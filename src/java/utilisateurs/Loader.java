package utilisateurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import utilisateurs.exceptions.LoadFileException;
import utilisateurs.exceptions.NoBackupFileException;
import utilisateurs.gestionnaires.*;

/**
 * Classe de chargement des donnees dans la structure objet du serveur. Les
 * fichiers lus ont ete generes par la classe {@link Writer}.
 *
 * @author ndelafor
 *
 */
public class Loader {

    private static String users_fname;

    private static String backup_suffix;
    private static String datarep_prefix;
    private static String backuprep_prefix;
    private static String baseDir;

    public static void init(String ressourceDir) {
        baseDir = ressourceDir;

        Properties prop = new Properties();
        try {
            File f = new File(".");

            System.out.println("chemin d'execution Loader= " + f.getAbsolutePath());
            if (Server.CheckFileExists(baseDir + File.separator + "users.xml")) {
                prop.load(new FileInputStream(baseDir + File.separator + "users.xml"));//conf" + File.separator + "server.properties"));
                users_fname = "users.xml";//prop.getProperty("users.filename");
                backup_suffix = "bck";//prop.getProperty("backup.suffix");
                datarep_prefix = "";//prop.getProperty("data.repository.prefix");
                backuprep_prefix = "bck_old";//prop.getProperty("backup.repository.prefix");
            } else {

                users_fname = "users.xml";
                datarep_prefix = "";
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Un problème est survenu pendant le chargement des données. \nLes fichiers de sauvegarde ont été restaurés.");
        }
    }

    /**
     * Restauration des fichiers de backup en cas de probleme (corruption de
     * fichier etc)
     */
    public static void restore() {
        try {
            restoreUsers();
            load();
        } catch (NoBackupFileException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers() throws LoadFileException {
        try {
            if (Server.CheckFileExists(baseDir + File.separator + "users.xml")) {
            File fusers = new File(baseDir + File.separator + datarep_prefix + users_fname);
            Server.uh = JAXB.unmarshal(fusers, UserHandler.class);
            System.out.print(fusers.getAbsolutePath());
            Server.uh.print();
            }else {

                users_fname = "users.xml";
                datarep_prefix = "";
            }
        } catch (DataBindingException e) {
            throw new LoadFileException(
                    "Erreur au chargement du fichier des utilisateurs");
        }
    }

    public static void restoreUsers() throws NoBackupFileException {
        File fusers = new File(baseDir + File.separator + backuprep_prefix + users_fname + backup_suffix);
        if (fusers.exists()) {
            fusers.renameTo(new File(datarep_prefix + users_fname));
        } else {
            throw new NoBackupFileException(
                    "Aucun fichier de sauvegarde pour n'a été trouvé pour les utilisateurs");
        }
    }
}

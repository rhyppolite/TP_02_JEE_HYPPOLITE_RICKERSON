package utilisateurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXB;


public class Writer {

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
            System.out.println("chemin d'execution Writer = " + f.getAbsolutePath());

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
            users_fname = "users.xml";
            datarep_prefix = "";
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    /**
     * Serialisation de toute la hierarchie d'objets
     * Les anciens fichiers sont préservés pour une restauration en cas de probleme voir {@link Loader}.
     * @throws IOException
     */
    public static void serialize() throws IOException {
        serializeUsers();
    }

    public static void serializeUsers() {
        File fusers = new File(baseDir + File.separator + datarep_prefix + users_fname);
        // On cree une copie de sauvegarde des fichiers precedents en cas de probleme
        fusers.renameTo(new File(baseDir + File.separator + backuprep_prefix + users_fname + backup_suffix));
        try {
            fusers.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Serialisation
        JAXB.marshal(Server.uh, fusers);
        System.out.print("Utilisateurs sérialisés");
        Server.uh.print();
        
    }
}

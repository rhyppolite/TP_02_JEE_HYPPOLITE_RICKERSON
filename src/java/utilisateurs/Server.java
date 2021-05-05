package utilisateurs;

import java.io.File;
import utilisateurs.modeles.User;
import utilisateurs.gestionnaires.UserHandler;

public class Server {

    /** Gestionnaire de {@link User} */
    static public UserHandler uh = new UserHandler();
    private static boolean initialized = false;

    public static void init(String ressourceDir) {
        if (!initialized) {
            Loader.init(ressourceDir);
            Writer.init(ressourceDir);

            Loader.load(); // Chargement des fichiers XML
            initialized = true;

        }
    }
    
    public static Boolean CheckFileExists(String _fileName) {
        // test to see if a file exists
        File file = new File(_fileName);
        return file.exists();
    }

}

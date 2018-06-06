package backup;

/**
 *
 * @author pedro
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class Backup {

   /* private static String url = "jdbc:mysql://neolig.com:3306/";
    //private static String url = "jdbc:mysql://localhost:3306/";
    private static String port = "3306";
    private static String database = "neoli831_teste";
    private static String user = "neoli831_teste";
    private static String pass = "teste";
*/

    private static String ip = "localhost";
   // private static String port = "3306";
    private static String database = "neoli831_teste";
    private static String user = "neoli831_teste";
    private static String pass = "teste";
   // private static String path = "/home/Admin/abc/";

    public static boolean exportar(String path) {
        String dumpCommand = "mysqldump " + database + " -h " + ip + " -u " + user + " -p" + pass;
        Runtime rt = Runtime.getRuntime();
        File test = new File(path);
        PrintStream ps;
        try {
            Process child = rt.exec(dumpCommand);
            ps = new PrintStream(test);
            InputStream in = child.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                ps.write(ch);
//System.out.write(ch); //to view it by console
            }

            InputStream err = child.getErrorStream();
            while ((ch = err.read()) != -1) {
                System.out.write(ch);
            }
        } catch (Exception exc) {
            return false;
        }

        return true;
    }

    public static boolean importar(String path) {
        try {
            String comando = "/usr/bin/mysql";
            ProcessBuilder pb = new ProcessBuilder(comando, "--user=" + user, "--password=" + pass, "localhost/" + database, "--execute=source " + path);
            pb.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}

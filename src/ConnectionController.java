import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionController {

    static Connection con = null;
    static String url;

    public static Connection getConnection() {
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("In the connection try 1 block");
            Class.forName("com.mysql.jdbc.Driver");

        } catch (Exception e) {
            System.out.println("Driver loading error");
        }
        //openCon();
        con = null;
        try {
            //	con=DriverManager.getConnection("jdbc:odbc:hr", "system","snehal");

            //Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("In the connection try 2 block");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectMain", "root", "");
            System.out.println("In the connection try 2=1 block");

        } catch (SQLException e) {
            System.out.println("In the connection catch sQL exception 1 block " + e);
        }
        return con;

    }


}

				
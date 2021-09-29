package librarymanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class establishes a connection to create an empty database.
 *
 * @author Ebrahim Vericain
 */
public class DBConnection {

    private static Connection connect;

    /**
     * Applying a Singleton connection (for unique connection).
     *
     * @return A unique connection.
     */
    public static Connection getInstance() {
        if (connect == null) {
            connect = createConnection();
        }
        return connect;
    }

    /**
     * Declares a path on the current machine where the new database will be
     * which connects it to this project.
     *
     * @return A Connection.
     */
    public static Connection createConnection() {
        // The directory in which the LibraryManagemnt database will be located in.
        String dbUrl = "jdbc:sqlite:C:\\SQLITE\\db\\LibraryManagement.db";
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(dbUrl);
            System.out.println("\t\t\t\t\tUnique connection has been successfully established!\n\n");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return conn;
    }
}

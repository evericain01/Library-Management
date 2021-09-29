package librarymanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Student class.
 *
 * @author Ebrahim Vericain
 */
public class Student {

    private int stID;
    private String name;
    private long contact;
    private final Connection conn = DBConnection.getInstance();

    /**
     * Constructor that takes in all the properties of the Student.
     *
     * @param stID The ID.
     * @param name The name.
     * @param contactNumber The contact number.
     */
    public Student(int stID, String name, long contactNumber) {
        this.stID = stID;
        this.name = name;
        this.contact = contactNumber;
    }

    /**
     * Constructor that takes in nothing (Used to initialize an empty student
     * within the controller class).
     */
    public Student() {
    }

    /**
     * Searches all books in the Books table by title then sorts then in SN
     * order.
     *
     * @param title The desired book title to be searched.
     * @return A list of books containing the desired title.
     */
    public List<Book> searchBookByTitle(String title) {
        List<Book> list = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE TITLE= '" + title
                    + "' ORDER BY SN;");
            while (rs.next()) {
                String sn = rs.getString("SN");
                String bookTitle = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String publisher = rs.getString("PUBLISHER");
                int quantity = rs.getInt("QUANTITY");
                int issuedQuantity = rs.getInt("ISSUEDQUANTITY");
                String dateOfPurchase = rs.getString("DATEOFPURCHASE");

                list.add(new Book(sn, bookTitle, author, publisher, quantity,
                        issuedQuantity, dateOfPurchase));
            }
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return list;
    }

    /**
     * Searches all books in the Books table by author name then sorts then in
     * SN order.
     *
     * @param name The desired book title to be searched.
     * @return A list of books containing the desired author name.
     */
    public List<Book> searchBookByName(String name) {
        List<Book> list = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE AUTHOR= '" + name
                    + "' ORDER BY SN;");
            while (rs.next()) {
                String sn = rs.getString("SN");
                String bookTitle = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String publisher = rs.getString("PUBLISHER");
                int quantity = rs.getInt("QUANTITY");
                int issuedQuantity = rs.getInt("ISSUEDQUANTITY");
                String dateOfPurchase = rs.getString("DATEOFPURCHASE");

                list.add(new Book(sn, bookTitle, author, publisher, quantity,
                        issuedQuantity, dateOfPurchase));
            }
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return list;
    }

    /**
     * Searches all books in the Books table by year then sorts then in SN
     * order.
     *
     * @param year The desired book title to be searched.
     * @return A list of books containing the desired year.
     */
    public List<Book> searchBookByYear(String year) {
        List<Book> list = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE SUBSTR(DATEOFPURCHASE, 1, 4)= '"
                    + year + "' ORDER BY SN;");

            while (rs.next()) {
                String sn = rs.getString("SN");
                String bookTitle = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String publisher = rs.getString("PUBLISHER");
                int quantity = rs.getInt("QUANTITY");
                int issuedQuantity = rs.getInt("ISSUEDQUANTITY");
                String dateOfPurchase = rs.getString("DATEOFPURCHASE");

                list.add(new Book(sn, bookTitle, author, publisher, quantity,
                        issuedQuantity, dateOfPurchase));
            }
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return list;
    }

    /**
     * Prints all the books in the Books table as a map.
     *
     * @return A map of all books.
     */
    public Map<String, String> viewCatalog() {
        
        // Made it into LinkedHashMap, so map can be sorted by SN (Key).
        Map<String, String> list = new LinkedHashMap();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Books ORDER BY SN;");

            while (rs.next()) {
                String sn = rs.getString("SN");
                String title = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String publisher = rs.getString("PUBLISHER");
                int quantity = rs.getInt("QUANTITY");
                int issuedQuantity = rs.getInt("ISSUEDQUANTITY");
                String dateOfPurchase = rs.getString("DATEOFPURCHASE");

                list.put(" SN: " + sn, "\n TITLE: " + title + "\n AUTHOR: " + author
                        + "\n PUBLISHER: " + publisher + "\n QUANTITY: " + quantity
                        + "\n ISSUEDQUANTITY: " + issuedQuantity + "\n DATEOFPURCHASE: "
                        + dateOfPurchase);
            }
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return list;
    }

    /**
     * Issues a book. Adds +1 to the "issued" attribute and -1 from the
     * "quantity" of the desired book. Then add the new entry into the
     * IssuedBooks table.
     *
     * @param b The desired book to be issued.
     * @return A boolean value to determine if the book was issued or not.
     */
    public boolean borrow(Book b) {
        try (Statement stmt = conn.createStatement()) {
            boolean isBorrowed = false;

            if (b.getQuantity() > 0) {
                // Updating book issued quantity by +1
                String updateIssuedQuantitySql = "UPDATE Books SET ISSUEDQUANTITY = ISSUEDQUANTITY + 1 WHERE SN= '" + b.getSn() + "';";
                stmt.executeUpdate(updateIssuedQuantitySql);

                // Updating book quantity by -1;              
                String updateQuantitySql = "UPDATE Books SET QUANTITY = QUANTITY - 1 WHERE SN= '" + b.getSn() + "';";
                stmt.executeUpdate(updateQuantitySql);

                // Getting the current date.
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();

                // Generating an id for this issued book. (so not 100% unqiue).
                int generatedId = (int) (Math.random() * 99999 + 1);

                // NOTE: Inputted set values for this student's ID, name and contact. 
                // I did this because this method does not take in a student (which will be the current user in this case). 
                String insertSql = "INSERT INTO IssuedBooks (ID, SN, STUID, STUNAME, STUCONTACT,"
                        + " ISSUEDDATE) VALUES (" + generatedId + ", '" + b.getSn() + "','"
                        + 12345 + "','" + "Current User (You)" + "','" + 1234567890
                        + "','" + dateFormat.format(date) + "');";

                stmt.executeUpdate(insertSql);
                isBorrowed = true;
            }
            return isBorrowed;

        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            return false;
        }
    }

    /**
     * Returns a book. Adds +1 to the "quantity" attribute and -1 from the
     * "issued" of the desired book. Then deletes that issued book from the
     * IssuedBooks table.
     *
     * @param b The desired book to be returned.
     * @return A boolean value to determine if the book was removed or not.
     */
    public boolean returnBook(Book b) {
        try (Statement stmt = conn.createStatement()) {
            // Updating book quantity by +1;              
            String updateQuantitySql = "UPDATE Books SET QUANTITY = QUANTITY + 1 WHERE SN= '" + b.getSn() + "';";
            stmt.executeUpdate(updateQuantitySql);

            // Updating book issued quantity by -1
            String updateIssuedQuantitySql = "UPDATE Books SET ISSUEDQUANTITY = ISSUEDQUANTITY - 1 WHERE SN= '" + b.getSn() + "';";
            stmt.executeUpdate(updateIssuedQuantitySql);

            String sql = "DELETE FROM IssuedBooks WHERE SN= '" + b.getSn() + "';";
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            return false;
        }
    }

    /**
     * Generates a String of this Student class.
     *
     * @return A String containing information about the student.
     */
    @Override
    public String toString() {
        return "Student{" + "stID=" + stID + ", name=" + name + ", contactNumber="
                + contact + '}';
    }

    /**
     * Gets the students ID.
     *
     * @return The ID.
     */
    public int getStID() {
        return stID;
    }

    /**
     * Gets the students name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the students contact number.
     *
     * @return The contact number.
     */
    public long getContact() {
        return contact;
    }
}

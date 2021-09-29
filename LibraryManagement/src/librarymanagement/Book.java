package librarymanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Book class.
 *
 * @author Ebrahim Vericain
 */
public class Book {

    private String sn;
    private String title;
    private String author;
    private String publisher;
    private int quantity;
    private int issuedQuantity;
    private String dateOfPurchase;
    private final Connection conn = DBConnection.getInstance();

    /**
     * Constructor that takes in all the properties of the book.
     *
     * @param sn The sn number.
     * @param title The title.
     * @param author The author.
     * @param publisher The publisher.
     * @param quantity The quantity.
     * @param issuedQuantity The issuedQuantity.
     * @param dateOfPurchase The dataOfPurchase.
     */
    public Book(String sn, String title, String author, String publisher, int quantity,
            int issuedQuantity, String dateOfPurchase) {
        this.sn = sn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
        this.issuedQuantity = issuedQuantity;
        this.dateOfPurchase = dateOfPurchase;
    }

    /**
     * Constructor that takes in nothing (Used to initialize an empty book
     * within the controller class).
     */
    public Book() {
    }

    /**
     * Adds a book the Books table with the current date.
     *
     * @param b The desired book to add.
     */
    public void addBook(Book b) {
        try (Statement stmt = conn.createStatement()) {
            // Getting the current date.
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            String sql = "INSERT INTO Books (SN, TITLE, AUTHOR, PUBLISHER, QUANTITY,"
                    + " ISSUEDQUANTITY, DATEOFPURCHASE) VALUES (" + b.getSn() + ", '"
                    + b.getTitle() + "','" + b.getAuthor() + "','" + b.getPublisher()
                    + "','" + b.getQuantity() + "','" + b.getIssuedQuantity() + "','"
                    + dateFormat.format(date) + "');";
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Issues a book from a student. Adds +1 to the "issued" attribute and -1
     * from the "quantity" of the desired book. Then add the new entry into the
     * IssuedBooks table.
     *
     * @param b The desired book to be picked from the student.
     * @param s The student.
     * @return A boolean value to determine if the book was issued or not.
     */
    public boolean issueBook(Book b, Student s) {
        try (Statement stmt = conn.createStatement()) {
            boolean isIssued = false;

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

                String sql = "INSERT INTO IssuedBooks (ID, SN, STUID, STUNAME, STUCONTACT,"
                        + " ISSUEDDATE) VALUES (" + generatedId + ", '" + b.getSn() + "','"
                        + s.getStID() + "','" + s.getName() + "','" + s.getContact()
                        + "','" + dateFormat.format(date) + "');";
                stmt.executeUpdate(sql);
                isIssued = true;
            }
            return isIssued;

        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            return false;
        }
    }

    /**
     * Returns a book from a student. Adds +1 to the "quantity" attribute and -1
     * from the "issued" of the desired book. Then deletes that issued book from
     * the IssuedBooks table.
     *
     * @param b The desired book to be return from the the student.
     * @param s The student.
     * @return A boolean value to determine if the book was removed or not.
     */
    public boolean returnBook(Book b, Student s) {
        try (Statement stmt = conn.createStatement()) {
            // Updating book quantity by +1;              
            String updateQuantitySql = "UPDATE Books SET QUANTITY = QUANTITY + 1 WHERE SN= '" + b.getSn() + "';";
            stmt.executeUpdate(updateQuantitySql);

            // Updating book issued quantity by -1
            String updateIssuedQuantitySql = "UPDATE Books SET ISSUEDQUANTITY = ISSUEDQUANTITY - 1 WHERE SN= '" + b.getSn() + "';";
            stmt.executeUpdate(updateIssuedQuantitySql);
            
            String sql = "DELETE FROM IssuedBooks WHERE SN= '" + b.getSn()
                    + "' AND STUID= '" + s.getStID() + "';";
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            return false;
        }
    }

    /**
     * Prints all the books in the Books table as a map.
     *
     * @return A map of all books.
     */
    public static Map<String, String> viewCatalog() {
        Connection connector = DBConnection.getInstance();

        // Made it into LinkedHashMap, so map can be sorted by SN (Key).
        Map<String, String> list = new LinkedHashMap();

        try (Statement stmt = connector.createStatement()) {
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
     * Prints all the books in the IssuedBooks table as a map.
     *
     * @return A map of all issued books.
     */
    public static Map<String, String> viewIssuedBooks() {
        Connection connector = DBConnection.getInstance();
        
        // Made it into LinkedHashMap, so map can be sorted by SN (Key).
        Map<String, String> list = new LinkedHashMap();

        try (Statement stmt = connector.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM IssuedBooks ORDER BY SN;");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String sn = rs.getString("SN");
                int stuId = rs.getInt("STUID");
                String stuName = rs.getString("STUNAME");
                long stuContact = rs.getInt("STUCONTACT");
                String issuedDate = rs.getString("ISSUEDDATE");

                list.put(" ID: " + id, "\n SN: " + sn + "\n STUID: " + stuId
                        + "\n STUNAME: " + stuName + "\n STUCONTACT: " + stuContact
                        + "\n ISSUEDDATE: " + issuedDate);
            }
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return list;
    }

    /**
     * Generates a String of this Book class.
     *
     * @return A String containing information about the book.
     */
    @Override
    public String toString() {
        return " SN: " + sn + "\n TITLE: " + title + "\n AUTHOR: " + author
                + "\n PUBLISHER: " + publisher + "\n QUANTITY: " + quantity
                + "\n ISSUEDQUANTITY: " + issuedQuantity + "\n DATEOFPURCHASE: "
                + dateOfPurchase + "\n";
    }

    /**
     * Gets the books sn number.
     *
     * @return The sn number.
     */
    public String getSn() {
        return sn;
    }

    /**
     * Gets the books title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the books author.
     *
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the books publisher.
     *
     * @return The publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Gets the books quantity.
     *
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the books issued quantity.
     *
     * @return The issued quantity.
     */
    public int getIssuedQuantity() {
        return issuedQuantity;
    }

    /**
     * Gets the books date of purchase.
     *
     * @return The date of purchase.
     */
    public String getDateOfPurchase() {
        return dateOfPurchase;
    }
}

package librarymanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller class of Book and Student. Contains various CRUD methods which
 * are oriented towards SQL query statements in order to retrieve and manipulate
 * data from a database.
 *
 * @author Ebrahim Vericain
 */
public class LibraryController extends SqlQueryMethods {

    private List<Book> bookModel;
    private LibraryView view;
    private final Connection connector = DBConnection.getInstance();

    /**
     * Constructor that takes in a List of book and the view.
     *
     * @param bookModel List of books.
     * @param view The view of the Library.
     */
    public LibraryController(List<Book> bookModel, LibraryView view) {
        this.bookModel = bookModel;
        this.view = view;
    }

    /**
     * Constructor only used for testing (JUNIT).
     */
    public LibraryController() {
    }

    /**
     * Creates and drops any existing Books table.
     */
    public void createBooksTable() {
        String booksTable = "CREATE TABLE Books "
                + "(SN              TEXT PRIMARY KEY, "
                + " TITLE           NVARCHAR(50)      NOT NULL, "
                + " AUTHOR          NVARCHAR(40)      NOT NULL, "
                + " PUBLISHER       NVARCHAR(40)      NOT NULL, "
                + " QUANTITY        INT, "
                + " ISSUEDQUANTITY  INT, "
                + " DATEOFPURCHASE  NVARCHAR(10)) ";

        try (Statement stmt = connector.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Books;");
            stmt.executeUpdate(booksTable);
        } catch (Exception ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Creates and drops any existing Students table.
     */
    public void createStudentsTable() {
        String studentTable = "CREATE TABLE Students "
                + "(STUID           INT PRIMARY KEY, "
                + " NAME            NVARCHAR(40), "
                + " CONTACT         BIGINT) ";

        try (Statement stmt = connector.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Students;");
            stmt.executeUpdate(studentTable);
        } catch (Exception ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Creates and drops any existing IssuedBooks table.
     */
    public void createIssuedBooksTable() {
        String issuedBooksTable = "CREATE TABLE IssuedBooks "
                + "(ID              INT PRIMARY KEY   NOT NULL, "
                + " SN              TEXT, "
                + " STUID           INT, "
                + " STUNAME         NVARCHAR(40), "
                + " STUCONTACT      BIGINT, "
                + " ISSUEDDATE      NVARCHAR(10), "
                + " CONSTRAINT FK_SN FOREIGN KEY (SN) REFERENCES Books(SN), "
                + " CONSTRAINT FK_STUID FOREIGN KEY (STUID) REFERENCES Students(STU_ID)) ";

        try (Statement stmt = connector.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS IssuedBooks;");
            stmt.executeUpdate(issuedBooksTable);
        } catch (Exception ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * This ADDITIONAL method adds books into the Books table (similarly to the
     * addBook() method), however, it does so WITHOUT applying the current date.
     * This method is only used for the sake of adding already made books that
     * have a date of their own as seen implemented in the driver class.
     *
     * @param b The desired book to add.
     */
    public void initializeBooks(Book b) {
        try (Statement stmt = connector.createStatement()) {
            String sql = "INSERT INTO Books (SN, TITLE, AUTHOR, PUBLISHER, QUANTITY,"
                    + " ISSUEDQUANTITY, DATEOFPURCHASE) VALUES (" + b.getSn() + ", '"
                    + b.getTitle() + "','" + b.getAuthor() + "','" + b.getPublisher()
                    + "','" + b.getQuantity() + "','" + b.getIssuedQuantity() + "','"
                    + b.getDateOfPurchase() + "');";
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * This ADDITIONAL method adds students into the Students table as seen
     * implemented in the driver class.
     *
     * @param s The desired student to add.
     */
    public void initializeStudents(Student s) {
        try (Statement stmt = connector.createStatement()) {
            String sql = "INSERT INTO Students (STUID, NAME, CONTACT) VALUES (" + s.getStID() + ", '"
                    + s.getName() + "','" + s.getContact() + "');";
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * This ADDITIONAL method prints all the students in the Students table as a
     * map as seen implemented in the driver class.
     *
     * @return A map of all student.
     */
    public Map<String, String> viewStudentList() {
        Map<String, String> list = new HashMap();

        try (Statement stmt = connector.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students ORDER BY STUID;");

            while (rs.next()) {
                int stuId = rs.getInt("STUID");
                String name = rs.getString("NAME");
                long contact = rs.getLong("CONTACT");

                list.put(" STUID: " + stuId, "\n NAME: " + name + "\n CONTACT: " + contact);
            }
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return list;
    }

    /**
     * Prints the updated map.
     *
     * @param map The desired map to be printed.
     */
    public void updateView(Map<String, String> map) {
        LibraryView.printData(map);
    }
//------------------------------------------------------------------------------
    // BOOK CLASS METHODS:
    
    /**
     * References the addBook() method from the Book class.
     *
     * @param b The desired book.
     */
    @Override
    public void addBook(Book b) {
        Book book = new Book();
        book.addBook(b);
    }

    /**
     * References the issueBook() method from the Book class.
     *
     * @param b The desired book to be issued.
     * @param s The student.
     * @return A boolean value to see if the book was issued or not.
     */
    @Override
    public boolean issueBook(Book b, Student s) {
        Book book = new Book();
        return book.issueBook(b, s);
    }

    /**
     * References the returnBook() method from the Book class.
     *
     * @param b The desired book to be returned.
     * @param s The student.
     * @return A boolean value to see if the book was removed or not.
     */
    @Override
    public boolean returnBook(Book b, Student s) {
        Book book = new Book();
        return book.returnBook(b, s);
    }

    /**
     * References the viewCatalog() method from the Book class.
     *
     * @return A map of all books.
     */
    public static Map<String, String> viewCatalog() {
        return Book.viewCatalog();
    }

    /**
     * References the viewIssuedBooks() method from the Book class.
     *
     * @return A map of all issued books.
     */
    public static Map<String, String> viewIssuedBooks() {
        return Book.viewIssuedBooks();
    }

//------------------------------------------------------------------------------
    // STUDENT CLASS METHODS:
    
    /**
     * References the searchBookByTitle() method from the Student class.
     *
     * @param title The desired title.
     * @return A list of books containing the desired title.
     */
    @Override
    public List<Book> searchBookByTitle(String title) {
        Student student = new Student();
        return student.searchBookByTitle(title);
    }

    /**
     * References the searchBookByName() method from the Student class.
     *
     * @param name The desired author name.
     * @return A list of books containing the desired author name.
     */
    @Override
    public List<Book> searchBookByName(String name) {
        Student student = new Student();
        return student.searchBookByName(name);
    }

    /**
     * References the searchBookByYear() method from the Student class.
     *
     * @param year The desired year.
     * @return A list of books containing the desired year.
     */
    @Override
    public List<Book> searchBookByYear(String year) {
        Student student = new Student();
        return student.searchBookByYear(year);
    }

    /**
     * References the viewCatalogByStudent() method from the Student class.
     *
     * @return A map of all students.
     */
    @Override
    public Map<String, String> viewCatalogByStudent() {
        Student student = new Student();
        return student.viewCatalog();
    }

    /**
     * References the borrow() method from the Student class.
     *
     * @param b The desired book to be issued.
     * @return A boolean value to determine if the book was issued or not.
     */
    @Override
    public boolean borrow(Book b) {
        Student student = new Student();
        return student.borrow(b);
    }

    /**
     * References the returnBook() method from the Student class.
     *
     * @param b The desired book to be returned.
     * @return A boolean value to determine if the book was removed or not.
     */
    @Override
    public boolean returnBook(Book b) {
        Student student = new Student();
        return student.returnBook(b);
    }
}

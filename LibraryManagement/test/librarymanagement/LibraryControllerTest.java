package librarymanagement;

import org.junit.Before;
import org.junit.Test;

/**
 * JUNIT. (Testing all controller methods).
 *
 * @author Ebrahim Vericain
 */
public class LibraryControllerTest {

    // Declaring and initializing a LibraryController object that takes in the book list
    private final LibraryController controllerMethod = new LibraryController();

    /**
     * Uses the createBooksTable() method from the LibraryController class in order
     * to initialize the Books table.
     */
    @Before
    public void createBooksTable() {
        controllerMethod.createBooksTable();
        System.out.println("LibraryController Class: method createBooksTable() --> PASSED.\n\n");
    }

    /**
     * Uses the createStudentTable() method from the LibraryController class in order
     * to initialize the Students table.
     */
    @Before
    public void createStudentTable() {
        controllerMethod.createStudentsTable();
        System.out.println("LibraryController Class: method createStudentTable() --> PASSED.\n\n");
    }

    /**
     * Uses the createIssuedBooksTable() method from the LibraryController class in order
     * to initialize the IssuedBooks table.
     */
    @Before
    public void createIssuedBooksTable() {
        controllerMethod.createIssuedBooksTable();
        System.out.println("LibraryController Class: method createCreateIssuedBooks() --> PASSED.\n\n");
    }

    /**
     * Uses the initializeBooks() method from the LibraryController class in order 
     * to add made already books in the in the Books table
     */
    @Before
    public void initializeBooks() {
        Book b1 = new Book("3219023909190", "Advanced Algorithms", "Amy Chang", "Lee Wing", 2, 1, "2013-07-21");
        Book b2 = new Book("8345345533667", "Cloud Computing I", "Justice Arthur", "Athena Bernard", 8, 9, "2016-09-30");
        Book b3 = new Book("5349923532569", "Data Science", "Ivor Foster", "Colt Albert", 0, 4, "2001-08-12");
        Book b4 = new Book("8429038409092", "Data Science", "John Ficher", "Heather Guy", 3, 4, "2001-03-01");
        Book b5 = new Book("3242348902934", "Intro to Database", "Abraham Colton", "Dimitri Volgov", 3, 0, "2001-11-12");
        Book b6 = new Book("8678538234545", "Data Structures II", "Abraham Colton", "Phillip Goodman", 9, 1, "1960-11-08");
        controllerMethod.initializeBooks(b1);
        controllerMethod.initializeBooks(b2);
        controllerMethod.initializeBooks(b3);
        controllerMethod.initializeBooks(b4);
        controllerMethod.initializeBooks(b5);
        controllerMethod.initializeBooks(b6);
        System.out.println(Book.viewCatalog());
        System.out.println("LibraryController Class: method initializeBooks() --> PASSED.\n\n");
    }

    /**
     * Uses the initializeStudents() method from the LibraryController class in order 
     * to add already made students in the in the Students table.
     */
    @Before
    public void initializeStudents() {
        Student s1 = new Student(70576, "Max Williams", 5140945862L);
        Student s2 = new Student(84234, "Anthony Vicent", 5148435096L);
        Student s3 = new Student(32004, "James Benjamin", 5149002743L);
        controllerMethod.initializeStudents(s1);
        controllerMethod.initializeStudents(s2);
        controllerMethod.initializeStudents(s3);
        System.out.println(controllerMethod.viewStudentList());
        System.out.println("LibraryController Class: method initializeStudents() --> PASSED.\n\n");

    }

    /**
     * Uses the viewStudentList() method in order to display the students list.
     */
    @Before
    public void viewStudentList() {
        System.out.println(controllerMethod.viewStudentList());
        System.out.println("LibraryController Class: method viewStudentList() --> PASSED.\n\n");
    }

//------------------------------------------------------------------------------
    // BOOK CLASS METHODS
    /**
     * Tests the addBook() method from the LibraryController class.
     */
    @Test
    public void testAddBook() {
        Book b1 = new Book("5672566546478", "Cloud Computing I", "Randolf Chancellor", "Wallace Peel", 2, 0, "2016-11-05");
        Book b2 = new Book("1204432486590", "Advanced Algorithms", "Alex Jones", "Dewayne Christian", 8, 1, "2012-02-20");
        controllerMethod.addBook(b1);
        controllerMethod.addBook(b2);
        System.out.println(Book.viewCatalog());
        System.out.println("LibraryController Class: method addBook() --> PASSED.\n\n");
    }

    /**
     * Tests the issueBook() method from the LibraryController class.
     */
    @Test
    public void testIssueBook() {
        Book book = new Book("1204432486590", "Advanced Algorithms", "Alex Jones", "Dewayne Christian", 8, 1, "2012-02-20");
        Student student = new Student(70576, "Max Williams", 5140945862L);
        controllerMethod.issueBook(book, student);
        System.out.println(Book.viewIssuedBooks());
        System.out.println("LibraryController Class: method issueBook() --> PASSED.\n\n");
    }

    /**
     * Tests the returnBook() method from the LibraryController class.
     */
    @Test
    public void testReturnBook() {
        Book book = new Book("1204432486590", "Advanced Algorithms", "Alex Jones", "Dewayne Christian", 8, 1, "2012-02-20");
        Student student = new Student(70576, "Max Williams", 5140945862L);
        controllerMethod.returnBook(book, student);
        System.out.println(Book.viewIssuedBooks());
        System.out.println("LibraryController Class: method returnBook() --> PASSED.\n\n");
    }

    /**
     * Tests the viewCatalog() method from the LibraryController class.
     */
    @Test
    public void testViewCatalog() {
        System.out.println(Book.viewCatalog());
        System.out.println("LibraryController Class: method viewCatalog() --> PASSED.\n\n");
    }

    /**
     * Tests the viewIsssuedBooks() method from the LibraryController class.
     */
    @Test
    public void testViewIssuedBooks() {
        System.out.println(Book.viewIssuedBooks());
        System.out.println("LibraryController Class: method viewIssuedBooks() --> PASSED.\n\n");
    }
//------------------------------------------------------------------------------
    // STUDENT CLASS METHODS

    /**
     * Tests the searchBookByTitle() method from the LibraryController class.
     */
    @Test
    public void testSearchBookByTitle() {
        String title = "Data Structures II";
        System.out.println(controllerMethod.searchBookByTitle(title));
        System.out.println("LibraryController Class: method searchBookByTitle() --> PASSED.\n\n");
    }

    /**
     * Tests the searchBookByName() method from the LibraryController class.
     */
    @Test
    public void testSearchBookByName() {
        String name = "Abraham Colton";
        System.out.println(controllerMethod.searchBookByName(name));
        System.out.println("LibraryController Class: method searchBookByName() --> PASSED.\n\n");
    }

    /**
     * Tests the searchBookByYear() method from the LibraryController class.
     */
    @Test
    public void testSearchBookByYear() {
        String year = "2001";
        System.out.println(controllerMethod.searchBookByYear(year));
        System.out.println("LibraryController Class: method searchBookByYear() --> PASSED.\n\n");
    }

    /**
     * Tests the viewCatalogByStudent() method from the LibraryController class.
     */
    @Test
    public void testViewCatalogByStudent() {
        System.out.println(controllerMethod.viewCatalogByStudent());
        System.out.println("LibraryController Class: method viewCatalogByStudent --> PASSED.\n\n");
    }

    /**
     * Tests the borrow() method from the LibraryController class.
     */
    @Test
    public void testBorrow() {
        Book book = new Book("3242348902934", "Intro to Database", "Abraham Colton", "Dimitri Volgov", 3, 0, "2001-11-12");
        controllerMethod.borrow(book);
        System.out.println(Book.viewIssuedBooks());
        System.out.println("LibraryController Class: method borrow() --> PASSED.\n\n");
    }

    /**
     * Tests the returnBookByStudent() method from the LibraryController class.
     */
    @Test
    public void testReturnBookByStudent() {
        Book book = new Book("3242348902934", "Intro to Database", "Abraham Colton", "Dimitri Volgov", 3, 0, "2001-11-12");
        controllerMethod.returnBook(book);
        System.out.println(Book.viewIssuedBooks());
        System.out.println("LibraryController Class: method returnBook() --> PASSED.\n\n");
    }
}

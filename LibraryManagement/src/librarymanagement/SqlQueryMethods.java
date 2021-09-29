package librarymanagement;

import java.util.List;
import java.util.Map;

/**
 * This abstract class utilizes the strategy pattern by override all these
 * methods in the LibraryController class.
 *
 * @author Ebrahim Vericain
 */
public abstract class SqlQueryMethods {

    public abstract void addBook(Book b);

    public abstract boolean issueBook(Book b, Student s);

    public abstract boolean returnBook(Book b, Student s);

    public abstract List<Book> searchBookByTitle(String title);

    public abstract List<Book> searchBookByName(String name);

    public abstract List<Book> searchBookByYear(String year);

    public abstract Map<String, String> viewCatalogByStudent();

    public abstract boolean borrow(Book b);

    public abstract boolean returnBook(Book b);
}

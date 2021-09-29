package librarymanagement;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * The Driver class. This class constructs a Library Management System that
 * utilizes all classes in this project.
 *
 * @author Ebrahim Vericain
 */
public class Driver {

    public static void main(String[] args) {
        List<Book> bookModel = bookData();
        List<Student> studentList = studentData();
        LibraryView view = new LibraryView();

        LibraryController controller = new LibraryController(bookModel, view);
        
        // Declaring scanner.
        Scanner console;

        //----------------------------------------------------------------------
        // Utilizing Locale and Resource Bundle for English and French.
        Locale local = new Locale("en", "CA");

        boolean languageRunner = true;

        // Application language chooser.
        while (languageRunner) {
            console = new Scanner(System.in);
            System.out.println("Enter \"1\", for English\nEntrez \"2\", pour Français");
            try {
                int languageChoice = console.nextInt();
                switch (languageChoice) {
                    case 1:
                        local = new Locale("en", "CA");
                        languageRunner = false;
                        break;
                    case 2:
                        local = new Locale("fr", "CA");
                        languageRunner = false;
                        break;
                    default:
                        System.out.println("\n******************\nEnter \"1\" or \"2\" for a valid choice."
                                + "\nEntrez \"1\" ou \"2\" pour un choix valide.\n********************\n");
                        break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("\n*******************************\nYou did not input "
                        + "an integer.\nVous n'avez pas entré de numéro."
                        + "\n*******************************\n");
            }
        }

        // Setting the resource bundle to the targeted locale (English/French).
        ResourceBundle bundle = ResourceBundle.getBundle("librarymanagement//Source", local);
        System.out.println();

        //----------------------------------------------------------------------
        
        // Creating SQL Books, Students and IssuedBooks table. And drops them if they exists.
        controller.createBooksTable();
        controller.createStudentsTable();
        controller.createIssuedBooksTable();

        //----------------------------------------------------------------------
        
        // Inserting already made books and students into their respective table.
        System.out.println(bundle.getString("loadingBooksMessage"));
        bookModel.forEach((b) -> {
            controller.initializeBooks(b);
        });
        controller.updateView(LibraryController.viewCatalog());

        System.out.println(bundle.getString("loadingStudentsMessage"));
        studentList.forEach((s) -> {
            controller.initializeStudents(s);
        });
        controller.updateView(controller.viewStudentList());

        //----------------------------------------------------------------------
        // Application Runner (Runs the entire Library Management System).
        System.out.print("----------------------------------------\n\n");
        System.out.println(bundle.getString("titleMessage"));

        boolean flagForUserSelection = true;

        while (flagForUserSelection) {
            try {
                console = new Scanner(System.in);
                System.out.println(bundle.getString("selectUserMessage"));
                int chooseUserType = console.nextInt();
                boolean flagForOptionSelection = true;
                switch (chooseUserType) {
                    case 0:
                        System.out.println();
                        while (flagForOptionSelection) {
                            try {
                                console = new Scanner(System.in);
                                System.out.println(bundle.getString("librarianSelectionMessage"));
                                int chooseLibrianOption = console.nextInt();
                                console.nextLine();
                                switch (chooseLibrianOption) {
                                    case 1:
                                        // Asks the user to input all Book's properties in order
                                        // to create the newly added book they want.
                                        System.out.println(bundle.getString("enterSnMessage"));
                                        String sn = console.nextLine();

                                        System.out.println(bundle.getString("enterBookTitleMessage"));
                                        String title = console.nextLine();

                                        System.out.println(bundle.getString("enterAuthorMessage"));
                                        String author = console.nextLine();

                                        System.out.println(bundle.getString("enterPublisherMessage"));
                                        String publisher = console.nextLine();

                                        System.out.println(bundle.getString("enterQuantityMessage"));
                                        int quantity = console.nextInt();
                                        console.nextLine();

                                        // Issued quantity is set to 0 and date is set to current date
                                        // (gets the current date from addBook() method).
                                        int issuedQuantity = 0;
                                        String date = "";

                                        controller.addBook(new Book(sn, title, author,
                                                publisher, quantity, issuedQuantity, date));
                                        System.out.println(bundle.getString("bookSuccessMessage"));
                                        break;
                                    case 2:
                                        // Issues a book.
                                        System.out.println(bundle.getString("selectingBookBySnMessage"));
                                        String sNumber = console.nextLine();

                                        System.out.println(bundle.getString("selectingStudentByIdMessage"));
                                        int stuId = console.nextInt();
                                        console.nextLine();

                                        // Goes thtough the Book List to get the specific book SN provided 
                                        // from the user, then goes through the Student List to get the 
                                        // specific student ID also provided from the user. 
                                        // Then issues that book with that student.
                                        ListIterator<Book> iteratingBook = bookModel.listIterator();
                                        while (iteratingBook.hasNext()) {
                                            Book book = iteratingBook.next();
                                            if (book.getSn().equals(sNumber)) {
                                                ListIterator<Student> iteratingStu = studentList.listIterator();
                                                while (iteratingStu.hasNext()) {
                                                    Student student = iteratingStu.next();
                                                    if (student.getStID() == stuId) {
                                                        if (book.getQuantity() <= 0) {
                                                            System.out.println(bundle.getString("noMoreBooksMessage"));
                                                            break;
                                                        } else {
                                                            controller.issueBook(book, student);
                                                            System.out.println(bundle.getString("bookIssuedMessage"));
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case 3:
                                        //Returns a book.
                                        System.out.println(bundle.getString("selectReturnStudentMessage"));
                                        int stuID = console.nextInt();
                                        console.nextLine();

                                        System.out.println(bundle.getString("selectReturnBookMessage"));
                                        String sN = console.nextLine();

                                        // Goes through the Student List to get the specifc student ID 
                                        // provided from the user, then goes through the Book List to get 
                                        // the specific book SN also provided from the user. 
                                        // The returns that book with that student.
                                        ListIterator<Student> iterateStu = studentList.listIterator();
                                        while (iterateStu.hasNext()) {
                                            Student student = iterateStu.next();
                                            if (student.getStID() == stuID) {
                                                ListIterator<Book> iterateBook = bookModel.listIterator();
                                                while (iterateBook.hasNext()) {
                                                    Book book = iterateBook.next();
                                                    if (book.getSn().equals(sN)) {
                                                        controller.returnBook(book, student);
                                                        System.out.println(bundle.getString("bookReturnedMessage"));
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case 4:
                                        // Updates the view (Loads book catalog).
                                        System.out.println();
                                        controller.updateView(Book.viewCatalog());
                                        break;
                                    case 5:
                                        // Updates the view (Loads issued books catalog).
                                        System.out.println();
                                        controller.updateView(Book.viewIssuedBooks());
                                        break;
                                    case 6:
                                        // Returns the user to user type selection (librarian or student).
                                        System.out.println();
                                        flagForOptionSelection = false;
                                        break;
                                    default:
                                        // Checks for invalid input.
                                        System.out.println();
                                        System.out.println(bundle.getString("invalidMessage1-6"));
                                        break;
                                }
                            } catch (InputMismatchException ex) {
                                System.out.println(bundle.getString("enterAnIntegerMessage"));
                            }
                        }
                        break;
                    case 1:
                        System.out.println();
                        while (flagForOptionSelection) {
                            try {
                                console = new Scanner(System.in);
                                System.out.println(bundle.getString("studentSelectionMessage"));
                                int chooseStudentOption = console.nextInt();
                                console.nextLine();

                                switch (chooseStudentOption) {
                                    case 1:
                                        // Searches for all books with the title that the user will provide.
                                        System.out.println(bundle.getString("enterSearchTitleMessage"));
                                        String title = console.nextLine();
                                        System.out.println();
                                        // Prints the list of all books with that title.
                                        controller.searchBookByTitle(title)
                                                .stream()
                                                .forEach(e -> System.out.println(e));
                                        break;
                                    case 2:
                                        // Searches for all books with the author name that the user will provide.
                                        System.out.println(bundle.getString("enterSearchNameMessage"));
                                        String authorName = console.nextLine();
                                        System.out.println();
                                        // Prints the list of all books that is written by that author.
                                        controller.searchBookByName(authorName)
                                                .stream()
                                                .forEach(e -> System.out.println(e));
                                        break;
                                    case 3:
                                        // Searches for all book with a year that the user will provide.
                                        System.out.println(bundle.getString("enterSearchYearMessage"));
                                        String year = console.next();
                                        System.out.println();
                                        // Prints the list of all books that were made in that year.
                                        controller.searchBookByYear(year)
                                                .stream()
                                                .forEach(e -> System.out.println(e));
                                        break;
                                    case 4:
                                        // Updates the view (Loads book catalog).
                                        System.out.println();
                                        controller.updateView(controller.viewCatalogByStudent());
                                        break;
                                    case 5:
                                        System.out.println(bundle.getString("selectingBookBySnMessage"));
                                        String sNumber = console.nextLine();

                                        // Goes through the Book List to get the specific SN 
                                        // that the user provided. Then lets the student 
                                        //(the user in this case) borrow that book.
                                        ListIterator<Book> iterator = bookModel.listIterator();
                                        while (iterator.hasNext()) {
                                            Book book = iterator.next();
                                            if (book.getSn().equals(sNumber)) {
                                                if (book.getQuantity() <= 0) {
                                                    System.out.println(bundle.getString("noMoreBooksMessage"));
                                                    break;
                                                } else {
                                                    controller.borrow(book);
                                                    System.out.println(bundle.getString("bookBorrowedMessage"));
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    case 6:
                                        System.out.println(bundle.getString("selectingBookBySnMessage"));
                                        String sNum = console.nextLine();

                                        // Goes through the Book List to get the specific SN 
                                        // that the user provided. Then lets the student
                                        // (the user in this case) return that book.
                                        ListIterator<Book> bookIterator = bookModel.listIterator();
                                        while (bookIterator.hasNext()) {
                                            Book book = bookIterator.next();
                                            if (book.getSn().equals(sNum)) {
                                                controller.returnBook(book);
                                                System.out.println(bundle.getString("bookReturnedMessage"));
                                            }
                                        }
                                        break;
                                    case 7:
                                        // Returns the user to user type selection (librarian or student).
                                        System.out.println();
                                        flagForOptionSelection = false;
                                        break;
                                    default:
                                        // Checks for invalid input.
                                        System.out.println();
                                        System.out.println(bundle.getString("invalidMessage1-7"));
                                        break;
                                }
                            } catch (InputMismatchException ex) {
                                System.out.println(bundle.getString("enterAnIntegerMessage"));
                            }
                        }
                        break;
                    case 2:
                        // Exits the application.
                        System.out.println();
                        System.exit(0);
                        break;
                    default:
                        // Check for invalid input.
                        System.out.println();
                        System.out.println(bundle.getString("invalidMessage1-3"));
                        break;
                }
            } catch (InputMismatchException ex) {
                System.out.println(bundle.getString("enterAnIntegerMessage"));
            }
        }
    }

    /**
     * Creating a List of books to be loaded into the books table.
     *
     * @return The List of books.
     */
    public static List<Book> bookData() {
        // Declaring and initializing a List of books.
        ArrayList<Book> bookList = new ArrayList<>();

        // Creating the books then adding them into the List.
        Book b1 = new Book("9482473847243", "Computing 101", "John Ficher", "Jennifer Colt", 0, 3, "1990-07-15");
        Book b2 = new Book("3242348902934", "Intro to Database", "Abraham Colton", "Dimitri Volgov", 3, 0, "2001-11-12");
        Book b3 = new Book("1204432486590", "Advanced Algorithms", "Alex Jones", "Dewayne Christian", 8, 1, "2012-02-20");
        Book b4 = new Book("2348908320948", "Data Science", "Abraham Colton", "Stephen Milton", 9, 2, "1999-01-22");
        Book b5 = new Book("1645986080942", "Data Structures II", "Edwin Archambault", "Lucas Nardius", 2, 0, "1990-09-08");
        Book b6 = new Book("7482394409824", "Cloud Computing I", "Alex Jones", "Sophia Liberty", 1, 6, "2013-12-19");
        Book b7 = new Book("8429038409092", "Data Science", "John Ficher", "Heather Guy", 3, 4, "2001-03-01");
        Book b8 = new Book("3219023909190", "Advanced Algorithms", "Amy Chang", "Lee Wing", 2, 1, "2013-07-21");
        Book b9 = new Book("3465345345435", "Computing 101", "Edwin Archambault", "Marie-Laure Brice", 7, 1, "2000-03-13");
        Book b10 = new Book("5672566546478", "Cloud Computing I", "Randolf Chancellor", "Wallace Peel", 2, 0, "2016-11-05");
        Book b11 = new Book("5349923532569", "Data Science", "Ivor Foster", "Colt Albert", 0, 4, "2001-08-12");
        Book b12 = new Book("9654645645622", "Advanced Algorithms", "Alex Jones", "Colin Lebrun", 8, 1, "1997-08-29");
        Book b13 = new Book("8345345533667", "Cloud Computing I", "Justice Arthur", "Athena Bernard", 8, 9, "2016-09-30");
        Book b14 = new Book("1545345345722", "Computing 101", "Alex Jones", "Merv Gage", 3, 5, "1960-02-23");
        Book b15 = new Book("8678538234545", "Data Structures II", "Abraham Colton", "Phillip Goodman", 9, 1, "1960-11-08");

        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);
        bookList.add(b4);
        bookList.add(b5);
        bookList.add(b6);
        bookList.add(b7);
        bookList.add(b8);
        bookList.add(b9);
        bookList.add(b10);
        bookList.add(b11);
        bookList.add(b12);
        bookList.add(b13);
        bookList.add(b14);
        bookList.add(b15);

        return bookList;
    }

    /**
     * Creating a List of Students.
     *
     * @return The List of Student.
     */
    public static List<Student> studentData() {
        // Declaring and initializing a List of students.
        ArrayList<Student> stuList = new ArrayList<>();

        // Creating the students then adding them into the List.
        Student s1 = new Student(43123, "Kyle Diggle", 5149485745L);
        Student s2 = new Student(23431, "Beth Karmon", 5144852747L);
        Student s3 = new Student(97824, "Tiffany Hill", 4385049574L);
        Student s4 = new Student(84234, "Anthony Vicent", 5148435096L);

        stuList.add(s1);
        stuList.add(s2);
        stuList.add(s3);
        stuList.add(s4);

        return stuList;
    }
}

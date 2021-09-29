package librarymanagement;

import java.util.Map;

/**
 * The view class. This class is made to display a map of the books.
 *
 * @author Ebrahim Vericain
 */
public class LibraryView {

    /**
     * Prints a map.
     *
     * @param map The desired map.
     */
    public static void printData(Map<String, String> map) {
        map.entrySet()
            .stream()
            .forEach(e -> System.out.println(e + "\n"));
    }
}

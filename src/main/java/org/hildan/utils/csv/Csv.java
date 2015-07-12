package org.hildan.utils.csv;

/**
 * A base class containing global constants and useful methods to deal with CSV
 * files.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class Csv {

    private static final String CSV_EXTENSION = ".csv";
    
    protected static final String CSV_DEFAULT_COL_SEP = ",";

    /**
     * Checks whether the given {@code filename} ends with the CSV extension.
     * 
     * @param filename
     *            The filename to check.
     * @throws NotACsvFileException
     *             If the filename does not end with the CSV extension.
     */
    public static void checkExtension(String filename) throws NotACsvFileException {
        if (filename == null) {
            throw new IllegalArgumentException("The filename to check may not be null.");
        }
        int extBegin = filename.lastIndexOf('.');
        if (extBegin == -1 || !filename.substring(extBegin).equalsIgnoreCase(CSV_EXTENSION)) {
            throw new NotACsvFileException("'" + filename + "' is not a CSV file.");
        }
    }

    /**
     * Remove the CSV extension from a filename.
     * 
     * @param filename
     *            The original filename with extension.
     * @return The same filename without extension.
     */
    public static String removeExtension(String filename) {
        checkExtension(filename);
        return filename.substring(0, filename.lastIndexOf('.'));
    }
}

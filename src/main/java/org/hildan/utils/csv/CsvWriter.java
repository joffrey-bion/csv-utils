package org.hildan.utils.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A {@code CsvReader} is a basic writer for CSV files. Its current implementation contains a
 * {@link BufferedWriter}.
 *
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class CsvWriter {

    private BufferedWriter out;

    private final String delimiter;

    /**
     * Creates a {@code CsvWriter} to write to the specified file.
     *
     * @param filename
     *            Output filename.
     * @throws IOException
     *             If an I/O error occurs.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public CsvWriter(String filename) throws IOException, NotACsvFileException {
        this(filename, Csv.CSV_DEFAULT_COL_SEP);
    }

    /**
     * Creates a {@code CsvWriter} to write to the specified file.
     *
     * @param filename
     *            Output filename.
     * @param delimiter
     *            The delimiter for the columns within the file.
     * @throws IOException
     *             If an I/O error occurs.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public CsvWriter(String filename, String delimiter) throws IOException, NotACsvFileException {
        Csv.checkExtension(filename);
        out = new BufferedWriter(new FileWriter(filename));
        this.delimiter = delimiter;
    }

    /**
     * Writes a row to the CSV file.
     *
     * @param cols
     *            An array of {@code String}s, each {@code String} representing a cell in the row to
     *            write.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public void writeRow(String[] cols) throws IOException {
        for (String c : cols) {
            out.write(c);
            if (c != cols[cols.length - 1]) {
                out.write(delimiter);
            }
        }
        out.write("\n");
    }

    /**
     * Closes this {@code CsvWriter}.
     */
    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

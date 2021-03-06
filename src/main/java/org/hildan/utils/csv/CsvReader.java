package org.hildan.utils.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A {@code CsvReader} is a basic reader for CSV files. Its current implementation contains a
 * {@link BufferedReader}.
 *
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class CsvReader implements Closeable {

    protected String filename;

    protected BufferedReader in;

    protected String[] lastReadRow;

    private final String delimiter;

    /**
     * Creates a {@code CsvReader} to read the specified file.
     *
     * @param filename
     *            Source filename.
     * @throws FileNotFoundException
     *             If the specified file is not found.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public CsvReader(String filename) throws FileNotFoundException, NotACsvFileException {
        this(filename, Csv.CSV_DEFAULT_COL_SEP);
    }

    /**
     * Creates a {@code CsvReader} to read the specified file.
     *
     * @param filename
     *            Source filename.
     * @param delimiter
     *            The delimiter for columns within the file.
     * @throws FileNotFoundException
     *             If the specified file is not found.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public CsvReader(String filename, String delimiter) throws FileNotFoundException, NotACsvFileException {
        Csv.checkExtension(filename);
        in = new BufferedReader(new FileReader(filename));
        this.filename = filename;
        this.delimiter = delimiter;
    }

    /**
     * Creates a {@code CsvReader} to read the specified stream.
     *
     * @param inputStream
     *            Source stream.
     * @param delimiter
     *            The delimiter for columns within the file.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public CsvReader(InputStream inputStream, String delimiter) {
        in = new BufferedReader(new InputStreamReader(inputStream));
        this.filename = null;
        this.delimiter = delimiter;
    }

    /**
     * Reads a row of the CSV file and returns it as an array containing a {@code String} for each
     * column.
     *
     * @return An array of {@code String}s, each {@code String} representing a cell in the read row,
     *         or {@code null} if the end of stream has been reached.
     * @throws IOException
     *             If an I/O error occurs.
     * @see #readRows(int)
     */
    public String[] readRow() throws IOException {
        try {
            String line = in.readLine();
            if (line == null) {
                lastReadRow = null;
            } else {
                lastReadRow = line.split(delimiter);
            }
            return lastReadRow;
        } catch (IOException e) {
            if (filename != null) {
                throw new IOException(e.getMessage() + " (file: '" + filename + "')", e);
            } else {
                throw e;
            }
        }
    }

    /**
     * Reads several rows and returns the last one. This method behaves exactly as if
     * {@link #readRow()} was called {@code nbRows} times. Called with 0 as its argument, this
     * method does not read any line, and returns the last line that was read by a previous call to
     * {@link #readRow()} or {@link #readRows(int)}. If the argument exceeds the number of lines in
     * the file, the reader goes to the end of the file and {@code null} is returned.
     *
     * @param nbRows
     *            The number of rows to read.
     * @return The last row that has been read, in the same format as {@link #readRow()} would.
     * @throws IOException
     *             If an I/O error occurs.
     * @see #readRow()
     */
    public String[] readRows(int nbRows) throws IOException {
        int n = nbRows;
        while (n > 0 && readRow() != null) {
            n--;
        }
        return lastReadRow;
    }

    /**
     * Closes this {@code CsvReader}.
     */
    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

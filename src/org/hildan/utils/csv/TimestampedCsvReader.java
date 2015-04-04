package org.hildan.utils.csv;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A {@link CsvReader} for CSV files having a timestamp for each row. The timestamp
 * can either be in one column, on spread on several columns (separate date and time
 * columns).
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public abstract class TimestampedCsvReader extends CsvReader {

    private final static int DEFAULT_BUFFER_SIZE = 1024;
    private final int rowBufferSize;

    /**
     * Opens a new {@code TimestampedCsvReader} for the specified file.
     * 
     * @param filename
     *            The path to the file to read.
     * @throws FileNotFoundException
     *             If the specified file does not exist.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public TimestampedCsvReader(String filename) throws FileNotFoundException,
            NotACsvFileException {
        this(filename, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Opens a new {@code TimestampedCsvReader} for the specified file.
     * 
     * @param filename
     *            The path to the file to read.
     * @param rowBufferSize
     *            The maximum number of characters contained in a row. A buffer of
     *            this size is allocated to be able to go back one row during a call
     *            to {@link #skipToReachTimestamp(long)}.
     * @throws FileNotFoundException
     *             If the specified file does not exist.
     * @throws NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public TimestampedCsvReader(String filename, int rowBufferSize) throws FileNotFoundException,
            NotACsvFileException {
        super(filename);
        this.rowBufferSize = rowBufferSize;
    }

    /**
     * Returns the timestamp of the specified row, in nanoseconds.
     * 
     * @param line
     *            The CSV row to extract the timestamp from.
     * @return the timestamp of the specified row, in nanoseconds.
     * @throws IOException
     *             If any I/O error occurs.
     */
    public abstract long extractTimestamp(String[] line) throws IOException;

    /**
     * Skips as many rows as necessary to reach the row with the specified timestamp.
     * The next row to be read will contain the first timestamp greater than or equal
     * to the specified timestamp.
     * 
     * @param timestamp
     *            The timestamp to reach, in nanoseconds.
     * @throws IOException
     *             If any I/O error occurs.
     */
    public void skipToReachTimestamp(long timestamp) throws IOException {
        String[] line;
        try {
            long currTimestamp;
            in.mark(rowBufferSize);
            while ((line = readRow()) != null) {
                currTimestamp = extractTimestamp(line);
                if (currTimestamp >= timestamp) {
                    in.reset();
                    return;
                }
                in.mark(rowBufferSize);
            }
        } catch (IOException e) {
            if (e.getMessage().equals("Mark invalid")) {
                throw new IOException("Last read row exceeded buffer size. (file: '" + filename + "')", e);
            } else {
                throw new IOException(e.getMessage() + " (file: '" + filename + "')", e);
            }
        }
    }
}

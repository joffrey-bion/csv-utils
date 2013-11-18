package com.jbion.utils.csv;

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

    /**
     * Opens a new {@code TimestampedCsvReader} for the specified file.
     * 
     * @param filename
     *            The path to the file to read.
     * @throws FileNotFoundException
     *             If the specified file does not exist.
     * @throws Csv.NotACsvFileException
     *             If the specified file is not a CSV file.
     */
    public TimestampedCsvReader(String filename) throws FileNotFoundException,
            Csv.NotACsvFileException {
        super(filename);
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
        long currTimestamp;
        in.mark(200);
        String[] line;
        while ((line = readRow()) != null) {
            currTimestamp = extractTimestamp(line);
            if (currTimestamp >= timestamp) {
                in.reset();
                break;
            }
            in.mark(200);
        }
    }
}

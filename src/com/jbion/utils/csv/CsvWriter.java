package com.jbion.utils.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.jbion.utils.csv.Csv.NotACsvFileException;

/**
 * A {@code CsvReader} is a basic writer for CSV files. Its current implementation
 * contains a {@link BufferedWriter}.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class CsvWriter {

    private BufferedWriter out;

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
        Csv.checkExtension(filename);
        out = new BufferedWriter(new FileWriter(filename));
    }

    /**
     * Writes a row to the CSV file.
     * 
     * @param cols
     *            An array of {@code String}s, each {@code String} representing a
     *            cell in the row to write.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public void writeRow(String[] cols) throws IOException {
        for (String c : cols) {
            out.write(c);
            if (c != cols[cols.length - 1]) {
                out.write(Csv.CSV_COL_SEP);
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

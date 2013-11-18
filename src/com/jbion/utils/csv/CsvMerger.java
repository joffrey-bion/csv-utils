package com.jbion.utils.csv;

import java.io.IOException;

import com.jbion.utils.csv.Csv.NotACsvFileException;

/**
 * A {@code CsvMerger} is used to merge CSV files with the same column headers into a
 * single file.
 * 
 * @author <a href="mailto:joffrey.bion@gmail.com">Joffrey BION</a>
 */
public class CsvMerger {

    private CsvWriter writer;

    /**
     * Creates a new CSV merger, setting the destination file.
     * 
     * @param destination
     *            The destination filename.
     * @throws IOException
     *             If an I/O error occurs.
     * @throws NotACsvFileException
     *             If the destination file is not a CSV file.
     */
    public CsvMerger(String destination) throws IOException, NotACsvFileException {
        writer = new CsvWriter(destination);
    }

    /**
     * Merges the specified CSV source files into the destination file given to the
     * constructor. The headers of the source files must exist and match.
     * 
     * @param csvSources
     *            An array of filenames.
     * @throws IOException
     *             If an I/O error occurs.
     * @throws NotACsvFileException
     *             If one of the source files is not a CSV file.
     */
    public void merge(String[] csvSources) throws IOException, NotACsvFileException {
        String[] header = null;
        CsvReader reader;
        for (String source : csvSources) {
            reader = new CsvReader(source);
            if (header == null) {
                // reading the first file, the headers have to be stored
                header = reader.readRow();
                if (header == null) {
                    throw new IOException("A source file is empty ('" + source + "'.");
                }
                writer.writeRow(header);
            } else {
                // the headers have to be the same as the first file's headers
                if (!equals(header, reader.readRow())) {
                    throw new RuntimeException("some source files don't have the same headers ("
                            + source + ")");
                }
            }
            // copy the rows
            String[] line;
            while ((line = reader.readRow()) != null) {
                writer.writeRow(line);
            }
            reader.close();
        }
        writer.close();
    }

    /**
     * Returns whether the strings in each array match.
     * 
     * @param h1
     *            First array of {@code String}s
     * @param h2
     *            Second array of {@code String}s
     * @return {@code true} if the arrays are of same length and {@code h1[i]} equals
     *         {@code h2[i]} for each i, {@code false} otherwise.
     */
    private static boolean equals(String[] h1, String[] h2) {
        if (h1.length != h2.length)
            return false;
        for (int i = 0; i < h1.length; i++) {
            if (!h1[i].equals(h2[i]))
                return false;
        }
        return true;
    }
}

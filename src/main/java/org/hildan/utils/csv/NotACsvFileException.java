package org.hildan.utils.csv;

@SuppressWarnings("serial")
public class NotACsvFileException extends RuntimeException {
    public NotACsvFileException(String message) {
        super(message);
    }
}
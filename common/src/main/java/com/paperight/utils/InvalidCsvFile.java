package com.paperight.utils;

public class InvalidCsvFile extends Exception {

    private static final long serialVersionUID = 1L;
    
    public InvalidCsvFile() {
        super();
    }
    
    public InvalidCsvFile(String message) {
        super(message);
    }

    public InvalidCsvFile(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCsvFile(Throwable cause) {
        super(cause);
    }

}

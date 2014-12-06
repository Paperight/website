package com.paperight.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

public class CsvUtils {
    
    public static void validateCsv(String filename, String[] expectedHeaders) throws Exception {
        CsvMapReader csvReader = null;
        try {
            csvReader = new CsvMapReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);

            String[] csvHeaders = csvReader.getHeader(true);
            if (csvHeaders.length != expectedHeaders.length) {
                throw new InvalidCsvFile("CSV column header count does not match the expected column header count. Expected: " + Arrays.toString(expectedHeaders) + " Found: " + Arrays.toString(csvHeaders));
            }
            boolean headersNamesValid = true;
            for (int i = 0; i < csvHeaders.length; i++) {
                String csvHeader = StringUtils.upperCase(csvHeaders[i]);
                String expectedHeader = StringUtils.upperCase(expectedHeaders[i]);
                if (!StringUtils.startsWith(csvHeader, expectedHeader)) {
                    headersNamesValid = false;
                    break;
                }
            }
            if (!headersNamesValid) {
                throw new InvalidCsvFile("CSV column headers does no match the expected column headers. Expected: " + Arrays.toString(expectedHeaders) + " Found: " + Arrays.toString(csvHeaders));
            }
            int lineNumber = 2;
            Map<String, String> csvLine = null;
            try {
                csvLine = csvReader.read(csvHeaders);
            } catch (Exception e) {
                String errorMessage = "Error on line:" + lineNumber;
                if (StringUtils.contains(e.getMessage(), "size")) {
                    errorMessage = errorMessage + " - column count does not match the expected column count";
                }
                throw new InvalidCsvFile(errorMessage);
            }

            while (csvLine != null ) {
                lineNumber += 1;
                try {
                    csvLine = csvReader.read(csvHeaders);
                } catch (Exception e) {
                    String errorMessage = "Error on line:" + lineNumber;
                    if (StringUtils.contains(e.getMessage(), "size")) {
                        errorMessage = errorMessage + " - column count does not match the expected column count";
                    }
                    throw new InvalidCsvFile(errorMessage);
                }
            }
        } finally {
            if( csvReader != null ) {
                csvReader.close();
            }
        }
    }
    
    public static void validateCsvHeaders(String filename, String[] expectedHeaders) throws Exception {
        CsvMapReader csvReader = null;
        try {
            csvReader = new CsvMapReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);

            String[] csvHeaders = csvReader.getHeader(true);
            if (csvHeaders.length != expectedHeaders.length) {
                throw new InvalidCsvFile("CSV column header count do not match the expected column header count");
            }
            for (int i = 0; i < csvHeaders.length; i++) {
                String csvHeader = StringUtils.upperCase(csvHeaders[i]);
                String expectedHeader = StringUtils.upperCase(expectedHeaders[i]);
                if (!StringUtils.startsWith(csvHeader, expectedHeader)) {
                    throw new InvalidCsvFile("CSV column headers do not match the expected column headers");
                }
            }
        } finally {
            if( csvReader != null ) {
                csvReader.close();
            }
        }
    }
    
    public static List<String> getCsvLines(String filename, String[] headers, boolean skipHeader) throws Exception {

        try (CsvMapReader csvReader = new CsvMapReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE)){
            List<String> result = new ArrayList<String>();
            if (skipHeader) {
                csvReader.getHeader(true);
            }
           
            while (csvReader.read(headers) != null ) {
               String rawRow = csvReader.getUntokenizedRow();
               result.add(rawRow);
            }
            return result;
        }  

    }
        
    public static List<String> getCsvLines2(String filename, boolean skipHeader) throws Exception {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(filename);
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            List<String> result = new ArrayList<String>();
            String line;
            if (skipHeader) {
                bufferedReader.readLine();
            }
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
            return result;
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(fileInputStream);
        }
    }
    
    public static Map<String, String> csvLineToMap(String csvLine, String[] keyNames) throws Exception {
        CsvMapReader csvReader = null;
        try {
            csvReader = new CsvMapReader(new StringReader(csvLine), CsvPreference.STANDARD_PREFERENCE);

            Map<String, String> result = csvReader.read(keyNames);
            if (result == null) {
                throw new InvalidCsvFile("No CSV line to process");
            }
            if (result.keySet().size() != keyNames.length) {
                throw new InvalidCsvFile("Invalid CSV line column count");
            }
            return result;
        } finally {
            if( csvReader != null ) {
                csvReader.close();
            }
        }
    }

}

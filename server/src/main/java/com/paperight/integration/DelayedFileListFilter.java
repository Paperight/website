package com.paperight.integration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.springframework.integration.file.filters.FileListFilter;

public class DelayedFileListFilter implements FileListFilter<File> {

    private String filenameRegex;

    private boolean initialised = false;

    @Override
    public List<File> filterFiles(File[] files) {
        List<File> accepted = new ArrayList<File>();
        if (files != null) {
            for (File file : files) {
                if (this.accept(file)) {
                    accepted.add(file);
                }
            }
        }
        return accepted;
    }

    public boolean accept(File file) {
        boolean result = false;
        if (file.getName().matches(getFilenameRegex())) {
            LocalDateTime fileLastModifiedDateTime = new LocalDateTime(file.lastModified());
            if (Minutes.minutesBetween(fileLastModifiedDateTime, LocalDateTime.now()).getMinutes() > 1) {
                result = true;
            }
        }
        return result;
    }

    public String getFilenameRegex() {
        return filenameRegex;
    }

    public void setFilenameRegex(String filenameRegex) {
        this.filenameRegex = filenameRegex;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

}

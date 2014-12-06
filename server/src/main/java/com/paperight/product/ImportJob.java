package com.paperight.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;

import com.paperight.AbstractEntity;
import com.paperight.Health;

@Entity
public class ImportJob extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    private String filename;
    private Status status = Status.NEW;
    private Health health = Health.OK;
    private String error;

    public enum Status {
        NEW, 
        PROCESSED;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }

    @Lob
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Column(nullable = false)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    @Transient
    public String getBaseFilename() {
        return FilenameUtils.getName(getFilename());
    }

}

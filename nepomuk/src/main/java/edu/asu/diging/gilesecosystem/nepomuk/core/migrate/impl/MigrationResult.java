package edu.asu.diging.gilesecosystem.nepomuk.core.migrate.impl;

import java.time.ZonedDateTime;

public class MigrationResult {

    private int migratedFiles;
    private ZonedDateTime finished;
    
    public MigrationResult(int migratedFiles, ZonedDateTime finished) {
        super();
        this.migratedFiles = migratedFiles;
        this.finished = finished;
    }
    
    public int getMigratedFiles() {
        return migratedFiles;
    }
    public void setMigratedFiles(int migratedFiles) {
        this.migratedFiles = migratedFiles;
    }
    public ZonedDateTime getFinished() {
        return finished;
    }
    public void setFinished(ZonedDateTime finshed) {
        this.finished = finshed;
    }
}

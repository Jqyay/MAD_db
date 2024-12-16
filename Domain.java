package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "domain",
        indices = {
                @Index(value = {"domainName"}, unique = true)
        }
        )
public class Domain {

    @PrimaryKey
    @NonNull
    private String domainId;

    @NonNull
    private String domainName;

    public Domain() {
    }

    // Constructor
    public Domain(@NonNull String domainId, @NonNull String domainName) {
        this.domainId = domainId;
        this.domainName = domainName;
    }

    // Getter and Setter for domainId
    @NonNull
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(@NonNull String domainId) {
        this.domainId = domainId;
    }

    // Getter and Setter for domainName
    @NonNull
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(@NonNull String domainName) {
        this.domainName = domainName;
    }
}

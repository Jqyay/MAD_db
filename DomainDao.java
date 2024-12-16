package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DomainDao {

    // Insert a single domain
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Domain domain);

    // Insert multiple domains
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Domain> domains);

    // Update an existing domain
    @Update
    void update(Domain domain);

    // Query to get all domains
    @Query("SELECT * FROM domain")
    List<Domain> getAll();

    // Query to get domain by domainId
    @Query("SELECT * FROM domain WHERE domainId = :domainId")
    Domain getById(String domainId);

    // Query to get domain by domainName
    @Query("SELECT * FROM domain WHERE domainName = :domainName")
    Domain getByName(String domainName);

    // Delete a specific domain
    @Delete
    void delete(Domain domain);

    // Query to delete all domains
    @Query("DELETE FROM domain")
    void deleteAll();
}

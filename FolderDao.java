package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FolderDao {

    // Insert a new Folder record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Folder folder);

    // Insert a new Folder record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Folder> folderList);

    @Update
    void update(Folder folder);

    // Query to get a Folder by its folderId
    @Query("SELECT * FROM folder WHERE folderId = :folderId")
    Folder getById(String folderId);

    // Query to get all folders for a specific userId
    @Query("SELECT * FROM folder WHERE userId = :userId")
    List<Folder> getByUserId(String userId);

    // Query to get all root folders (those with no parent folder)
    @Query("SELECT * FROM folder WHERE rootFolder IS NULL")
    List<Folder> getAllRootFolders();

    // Query to get all subfolders of a given root folder
    @Query("SELECT * FROM folder WHERE rootFolder = :rootFolderId")
    List<Folder> getSubfoldersByRootId(String rootFolderId);

    // Delete a Folder record
    @Delete
    void delete(Folder folder);

    @Query("DELETE FROM folder")
    void deleteAll();
}

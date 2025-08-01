package com.example.demo.repository;

import com.example.demo.model.Calendar;
import com.example.demo.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    @Query("SELECT new com.example.demo.model.FileEntity(fe.id, fe.fileName, fe.fileData) " +
            "FROM FileEntity fe " +
            "JOIN ProfileFileEntity pf ON pf.fileId = fe.id " +
            "JOIN UserProfile up ON pf.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<FileEntity> findFileByUserId(@Param("userId") Long userId);


    @Query("SELECT new com.example.demo.model.FileEntity(fe.fileName) " +
            "FROM FileEntity fe " +
            "JOIN ProfileFileEntity pf ON pf.fileId = fe.id " +
            "JOIN UserProfile up ON pf.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<FileEntity> findFileNamesByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.FileEntity(fe.id, fe.fileName, fe.fileData) " +
            "FROM FileEntity fe " +
            "JOIN ProfileFileEntity pf ON pf.fileId = fe.id " +
            "JOIN UserProfile up ON pf.profileId = up.profileId " +
            "WHERE up.userId = :userId AND fe.fileName = :fileName")
    FileEntity findByFileNameAndUserId(@Param("userId") Long userId,
                                            @Param("fileName") String fileName);
}

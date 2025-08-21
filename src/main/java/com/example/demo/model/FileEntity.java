package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"files\"")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;



    public FileEntity() {
    }

    public FileEntity(String fileName) {
        this.fileName = fileName;
        this.fileData = null;
    }


    public FileEntity(Long id, String fileName, byte[] fileData) {
        this.id = id;
        this.fileName = fileName;
        this.fileData = fileData;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
            private String fileName;
            private byte[] fileData;

            public Builder fileName(String fileName) {
                this.fileName = fileName;
                return this;
            }

            public Builder fileData(byte[] fileData) {
                this.fileData = fileData;
                return this;
            }

            public FileEntity build() {
                FileEntity fileEntity = new FileEntity();
                fileEntity.fileName = this.fileName;
                fileEntity.fileData = this.fileData;
                return fileEntity;
            }
    }

}

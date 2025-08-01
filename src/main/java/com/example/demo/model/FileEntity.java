package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

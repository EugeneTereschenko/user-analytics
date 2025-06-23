package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"project\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "project_name", length = 255)
    private String projectName;

    @Column(name = "date_from", length = 255)
    private String dateFrom;

    @Column(name = "date_to", length = 255)
    private String dateTo;

    @Column(name = "structure", length = 255)
    private String structure;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String projectName;
        private String dateFrom;
        private String dateTo;
        private String structure;

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder dateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public Builder dateTo(String dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public Builder structure(String structure) {
            this.structure = structure;
            return this;
        }

        public Project build() {
            Project project = new Project();
            project.projectName = this.projectName;
            project.dateFrom = this.dateFrom;
            project.dateTo = this.dateTo;
            project.structure = this.structure;
            return project;
        }
    }
}

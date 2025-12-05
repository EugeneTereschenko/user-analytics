package com.example.demo.dto;

import lombok.Data;

@Data
public class ProjectDTO {
    public String projectName;
    public String dateFrom;
    public String dateTo;
    public String structure;

    public static ProjectDTO.Builder builder() {
        return new ProjectDTO.Builder();
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

        public ProjectDTO build() {
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.projectName = this.projectName;
            projectDTO.dateFrom = this.dateFrom;
            projectDTO.dateTo = this.dateTo;
            projectDTO.structure = this.structure;
            return projectDTO;
        }
    }
}

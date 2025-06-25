package com.example.demo.dto;

import lombok.Data;

@Data
public class SkillsDTO {
    public String programmingLanguages;
    public String webFrameworks;
    public String devOps;
    public String sql;
    public String vcs;
    public String tools;

    public static SkillsDTO.Builder builder() {
        return new SkillsDTO.Builder();
    }

    public static class Builder {
        private String programmingLanguages;
        private String webFrameworks;
        private String devOps;
        private String sql;
        private String vcs;
        private String tools;

        public Builder programmingLanguages(String programmingLanguages) {
            this.programmingLanguages = programmingLanguages;
            return this;
        }

        public Builder webFrameworks(String webFrameworks) {
            this.webFrameworks = webFrameworks;
            return this;
        }

        public Builder devOps(String devOps) {
            this.devOps = devOps;
            return this;
        }

        public Builder sql(String sql) {
            this.sql = sql;
            return this;
        }

        public Builder vcs(String vcs) {
            this.vcs = vcs;
            return this;
        }

        public Builder tools(String tools) {
            this.tools = tools;
            return this;
        }

        public SkillsDTO build() {
            SkillsDTO skillsDTO = new SkillsDTO();
            skillsDTO.programmingLanguages = this.programmingLanguages;
            skillsDTO.webFrameworks = this.webFrameworks;
            skillsDTO.devOps = this.devOps;
            skillsDTO.sql = this.sql;
            skillsDTO.vcs = this.vcs;
            skillsDTO.tools = this.tools;
            return skillsDTO;
        }
    }
}

package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"skills\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "programming_languages", length = 255)
    private String programmingLanguages;

    @Column(name = "web_frameworks", length = 255)
    private String webFrameworks;

    @Column(name = "dev_ops", length = 255)
    private String devOps;

    @Column(name = "sql", length = 255)
    private String sql;

    @Column(name = "vcs", length = 255)
    private String vcs;

    @Column(name = "tools", length = 255)
    private String tools;

    public static Builder builder() {
        return new Builder();
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

        public Skills build() {
            Skills skills = new Skills();
            skills.programmingLanguages = this.programmingLanguages;
            skills.webFrameworks = this.webFrameworks;
            skills.devOps = this.devOps;
            skills.sql = this.sql;
            skills.vcs = this.vcs;
            skills.tools = this.tools;
            return skills;
        }
    }
}

package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeatureUsageDTO {
    private List<String> features;
    private List<Integer> usageCounts;


    public static class Builder {
        private List<String> features;
        private List<Integer> usageCounts;

        public Builder features(List<String> features) {
            this.features = features;
            return this;
        }

        public Builder usageCounts(List<Integer> usageCounts) {
            this.usageCounts = usageCounts;
            return this;
        }

        public FeatureUsageDTO build() {
            FeatureUsageDTO featureUsageDTO = new FeatureUsageDTO();
            featureUsageDTO.features = this.features;
            featureUsageDTO.usageCounts = this.usageCounts;
            return featureUsageDTO;
        }
    }
}

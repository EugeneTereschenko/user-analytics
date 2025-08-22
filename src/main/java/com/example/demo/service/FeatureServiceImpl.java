package com.example.demo.service;

import com.example.demo.dto.FeatureUsageDTO;
import com.example.demo.service.impl.FeatureService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FeatureServiceImpl implements FeatureService {

    @Override
    public FeatureUsageDTO getFeatureUsage() {
        return new FeatureUsageDTO.Builder()
                .features(List.of("Feature A", "Feature B", "Feature C"))
                .usageCounts(List.of(100, 200, 150))
                .build();
    }
}

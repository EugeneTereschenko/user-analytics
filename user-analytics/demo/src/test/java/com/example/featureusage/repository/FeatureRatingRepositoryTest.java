/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.repository;

import com.example.demo.UserAnalyticsJavaApplication;
import com.example.featureusage.model.FeatureRating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UserAnalyticsJavaApplication.class})
class FeatureRatingRepositoryTest {

    @Autowired
    private FeatureRatingRepository featureRatingRepository;

    @Test
    @DisplayName("findByFeatureName returns correct ratings")
    void testFindByFeatureName() {
        FeatureRating rating1 = new FeatureRating(null, 1L, "Dashboard", 4, null, null);
        FeatureRating rating2 = new FeatureRating(null, 2L,"Dashboard", 5, null, null);
        FeatureRating rating3 = new FeatureRating(null, 3L,"Reports", 3, null, null);
        featureRatingRepository.save(rating1);
        featureRatingRepository.save(rating2);
        featureRatingRepository.save(rating3);

        List<FeatureRating> dashboardRatings = featureRatingRepository.findByFeatureName("Dashboard");
        assertEquals(2, dashboardRatings.size());
        assertTrue(dashboardRatings.stream().allMatch(r -> r.getFeatureName().equals("Dashboard")));
    }

    @Test
    @DisplayName("getAverageRating returns correct average")
    void testGetAverageRating() {
        featureRatingRepository.save(new FeatureRating(null,1L, "Dashboard", 4, null, null));
        featureRatingRepository.save(new FeatureRating(null,2L, "Dashboard", 5, null, null));
        featureRatingRepository.save(new FeatureRating(null,3L, "Reports", 3, null, null));

        Double avg = featureRatingRepository.getAverageRating();
        assertNotNull(avg);
        assertEquals(4.0, avg, 0.01);
    }

    @Test
    @DisplayName("getAverageRatingPerFeature returns correct averages per feature")
    void testGetAverageRatingPerFeature() {
        featureRatingRepository.save(new FeatureRating(null,1L, "Dashboard", 4, null, null));
        featureRatingRepository.save(new FeatureRating(null,2L, "Dashboard", 2, null, null));
        featureRatingRepository.save(new FeatureRating(null,3L, "Reports", 5, null, null));

        List<Object[]> result = featureRatingRepository.getAverageRatingPerFeature();
        assertEquals(2, result.size());
        for (Object[] row : result) {
            String featureName = (String) row[0];
            Double avg = (Double) row[1];
            if ("Dashboard".equals(featureName)) {
                assertEquals(3.0, avg, 0.01);
            } else if ("Reports".equals(featureName)) {
                assertEquals(5.0, avg, 0.01);
            } else {
                fail("Unexpected feature: " + featureName);
            }
        }
    }

}
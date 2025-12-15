/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.repository;

import com.example.featureusage.model.FeatureRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRatingRepository extends JpaRepository<FeatureRating, Long> {

    List<FeatureRating> findByFeatureName(String featureName);

    @Query("SELECT AVG(fr.rating) FROM FeatureRating fr")
    Double getAverageRating();

    @Query("SELECT fr.featureName, AVG(fr.rating) FROM FeatureRating fr " +
            "GROUP BY fr.featureName")
    List<Object[]> getAverageRatingPerFeature();
}

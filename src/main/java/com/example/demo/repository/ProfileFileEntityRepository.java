package com.example.demo.repository;

import com.example.demo.model.ProfileFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileFileEntityRepository extends JpaRepository<ProfileFileEntity, Long> {

}

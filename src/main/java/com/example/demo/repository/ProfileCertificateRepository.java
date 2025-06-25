package com.example.demo.repository;

import com.example.demo.model.ProfileCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCertificateRepository extends JpaRepository<ProfileCertificate, Long> {
}

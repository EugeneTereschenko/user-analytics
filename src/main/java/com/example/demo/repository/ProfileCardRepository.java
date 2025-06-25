package com.example.demo.repository;


import com.example.demo.model.ProfileCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCardRepository extends JpaRepository<ProfileCard, Long> {
}

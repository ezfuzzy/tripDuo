package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.UserReport;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

}
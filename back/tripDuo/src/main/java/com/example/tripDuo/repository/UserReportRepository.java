package com.example.tripDuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tripDuo.entity.Report;

public interface UserReportRepository extends JpaRepository<Report, Long> {

}
package com.example.tripDuo.entity;

import java.time.LocalDateTime;

import com.example.tripDuo.enums.ReportStatus;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
@Entity
@Table(name="reports")
// 상속받는 모든 엔티티의 데이터를 하나의 테이블에 저장
// 조인이 필요 없기 때문에 조회 성능이 좋습니다. 하지만 테이블이 커질 수 있습니다.
// 테이블 구조가 단순해지지만, 모든 서브클래스의 필드를 포함해야 하므로 NULL 값이 많이 생길 수 있습니다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 상속받는 각 엔티티를 구분하는 컬럼을 생성
@DiscriminatorColumn(name = "target_type")
public abstract class Report {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private long reporterId; // 신고한 유저 id
	
	private String content;
	
	@Enumerated(EnumType.STRING)
    private ReportStatus status;
	
	private LocalDateTime createdAt;

	@PrePersist
    public void onPrePersist() {
        createdAt = LocalDateTime.now();
    }
}
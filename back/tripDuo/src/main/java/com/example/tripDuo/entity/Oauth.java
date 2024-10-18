package com.example.tripDuo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="oauth") // 인덱스 추가 
public class Oauth {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	@Column(nullable = false, length = 10)
	private String oauth_provider;
	@Column(nullable = false, length = 30)
	private String oauth_id;
	
}

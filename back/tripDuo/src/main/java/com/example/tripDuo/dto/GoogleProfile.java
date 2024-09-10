package com.example.tripDuo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleProfile {
	   // 구글 프로필에서의 사용자 ID
    private String id;

    // 사용자 이름
    private String name;

    // 사용자 이름 (주어진 이름)
    @JsonProperty("given_name")
    private String givenName;

    // 사용자 성
    @JsonProperty("family_name")
    private String familyName;

    // 프로필 사진 URL
    private String picture;

    // 이메일
    private String email;

    // 이메일 인증 여부
    @JsonProperty("email_verified")
    private boolean emailVerified;

    // 사용자 지역
    private String locale;

    // 기본 생성자
    public GoogleProfile() {
    }

}

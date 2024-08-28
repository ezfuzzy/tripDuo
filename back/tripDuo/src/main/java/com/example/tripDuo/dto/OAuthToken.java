package com.example.tripDuo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
public class OAuthToken {
    @JsonProperty("access_token")
	private String access_token;

    @JsonProperty("token_type")
	private String token_type;
    
    @JsonProperty("refresh_token")
	private String refresh_token;
    
	private String id_token;
	
	@JsonProperty("expires_in")
	private int expires_in;
    @JsonProperty("refresh_token_expires_in")

	private int refresh_token_expires_in;
	private String scope;
}

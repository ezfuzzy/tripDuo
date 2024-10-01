package com.example.tripDuo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.tripDuo.dto.ChatMessageDto;

@Configuration
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
		return new LettuceConnectionFactory(config);
	}

	@Bean
	public RedisTemplate<String, ChatMessageDto> redisTemplate(RedisConnectionFactory connectionFactory) {
		// Redis 서버와의 연결을 관리
		RedisTemplate<String, ChatMessageDto> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// 직렬화 설정 (선택 사항)
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return template;
	}
}

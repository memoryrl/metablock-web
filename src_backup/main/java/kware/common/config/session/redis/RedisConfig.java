package kware.common.config.session.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.List;


@Configuration
@EnableRedisHttpSession(redisNamespace = "${spring.session.redis.namespace}")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.session.store-type", havingValue = "redis")
public class RedisConfig {
    private static final int sessionTimeoutSeconds = 3600;

    @Value("${spring.redis.cluster.nodes}")
    private List<String> clusterNodes;

    @Primary
    @Bean
    public RedisIndexedSessionRepository overriddenSessionRepository(RedisIndexedSessionRepository sessionRepository) {
        sessionRepository.setDefaultMaxInactiveInterval(sessionTimeoutSeconds);
        return sessionRepository;
    }

    @Bean
    public SessionRegistry springSessionBackedSessionRegistry(RedisIndexedSessionRepository repository) {
        return new SpringSessionBackedSessionRegistry<>(repository);
    }

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        RedisConfiguration redisConfiguration;

        if(clusterNodes.size() == 1){
            String[] split = clusterNodes.get(0).split(":");
            redisConfiguration = new RedisStandaloneConfiguration(split[0], Integer.parseInt(split[1]));
        } else {
            redisConfiguration = new RedisClusterConfiguration(clusterNodes);
        }
        return new LettuceConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(new ObjectMapper()));
        return redisTemplate;
    }
}

package com.feng.redis.properties;

import com.feng.redis.ratelimit.RedisRateLimitImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "application.rate.limit.redis")
public class RedisRateLimitProperties {
    private List<RedisRateLimitImpl.LimiterInfo> limiterInfos;
}

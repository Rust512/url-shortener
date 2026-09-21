package com.training.url_shortener.aspect;

import com.training.url_shortener.annotation.DynamicTtlCacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DynamicTtlCacheAspect {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final ParameterNameDiscoverer nameDiscoverer = new StandardReflectionParameterNameDiscoverer();

    @Around("@annotation(com.training.urlshortener.annotation.DynamicTtlCacheable)")
    public Object handleCache(ProceedingJoinPoint joinPoint, DynamicTtlCacheable dynamicTtlCacheable) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();

        var evaluationContext = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        if (paramNames == null) {
            paramNames = new String[0];
        }

        Object[] args = joinPoint.getArgs();
        if (args == null) {
            args = new Object[0];
        }

        for (int i = 0; i < paramNames.length; i++) {
            evaluationContext.setVariable(paramNames[i], args[i]);
        }

        var evaluatedKey = parser.parseExpression(dynamicTtlCacheable.key()).getValue(evaluationContext, String.class);
        String redisKey = String.format("%s::%s", dynamicTtlCacheable.value(), evaluatedKey);

        Object cached = redisTemplate.opsForValue()
                .get(redisKey);

        if (cached != null) {
            log.debug("value for key {} retrieved from cache", evaluatedKey);
            return cached;
        }

        Object result = joinPoint.proceed();

        if (result != null) {
            redisTemplate.opsForValue().set(
                    redisKey,
                    result,
                    Expiration.from(Duration.of(dynamicTtlCacheable.ttl(),
                            dynamicTtlCacheable.timeUnit()))
            );
        }

        return result;
    }
}

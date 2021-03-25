package com.fsy.javasrc.annotation.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author fengsy
 * @date 3/10/21
 * @Description
 */
@Aspect
@Component
public class CacheAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Around("execution(* com.fsy.javasrc.*.*.*(..))")
    public Object lzcCacheAspect(ProceedingJoinPoint jp) throws Throwable {

        Class<?> cls = jp.getTarget().getClass();
        String methodName = jp.getSignature().getName();

        Map<String, Object> map = isChache(cls, methodName);
        boolean isCache = (boolean)map.get("isCache");

        if (isCache) {
            String cacheName = (String)map.get("cacheName"); // 缓存名字
            String key = (String)map.get("key"); // 自定义缓存key
            long timeOut = (long)map.get("timeOut"); // 过期时间， 0代表永久有效
            TimeUnit timeUnit = (TimeUnit)map.get("timeUnit"); // 过期时间单位
            Class<?> methodReturnType = (Class<?>)map.get("methodReturnType"); // 方法的返回类型
            Method method = (Method)map.get("method"); // 方法

            String realCacheName = "";
            // 判断cacheName是否为空，如果cacheName为空则使用默认的cacheName
            if (cacheName.equals("")) {
                realCacheName = cls.getName() + "." + methodName;
            } else {
                realCacheName = cacheName;
            }

            String realKey = "";
            // 判断key是否为空， 如果为空则使用默认的key
            if (key.equals("")) {
                realKey = realCacheName + "::" + defaultKeyGenerator(jp);
            } else {
                realKey = realCacheName + "::" + parseKey(key, method, jp.getArgs());
            }

            // 判断缓存中是否存在该key, 如果存在则直接从缓存中获取数据并返回
            if (stringRedisTemplate.hasKey(realKey)) {
                String value = stringRedisTemplate.opsForValue().get(realKey);
                return JsonUtil.toBean(methodReturnType, value);
            } else {
                Object result = jp.proceed();
                // 将返回结果保存到缓存中
                if (timeOut == 0) {
                    stringRedisTemplate.opsForValue().set(realKey, JsonUtil.toJson(result));
                } else {
                    stringRedisTemplate.opsForValue().set(realKey, JsonUtil.toJson(result), timeOut, timeUnit);
                }
                return result;
            }
        }
        return jp.proceed();
    }

    /**
     * 自定义生成key，使用方法中的参数作为key
     */
    private String defaultKeyGenerator(ProceedingJoinPoint jp) {
        // 获取所有参数的值
        List<String> list = new ArrayList<>();
        Object[] args = jp.getArgs();
        for (Object object : args) {
            list.add(object.toString());
        }
        return list.toString();
    }

    /**
     * 获取缓存的key key 定义在注解上，支持SPEL表达式
     */
    private String parseKey(String key, Method method, Object[] args) {
        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    private Map<String, Object> isChache(Class<?> cls, String methodName) {
        boolean isCache = false;
        Map<String, Object> map = new HashMap<>();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.isAnnotationPresent(Cache.class)) {
                Cache lzcCache = method.getAnnotation(Cache.class); // 获取方法上的注解
                Class<?> methodReturnType = method.getReturnType(); // 获取方法的返回类型
                map.put("cacheName", lzcCache.cacheName());
                map.put("key", lzcCache.key());
                map.put("timeOut", lzcCache.timeOut());
                map.put("timeUnit", lzcCache.timeUnit());
                map.put("methodReturnType", methodReturnType);
                map.put("method", method);
                isCache = true;
                break;
            }
        }
        map.put("isCache", isCache);
        return map;
    }
}
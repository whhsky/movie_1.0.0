package com.movie.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 指定缓存的失效时间
     *
     * @param key  redis中的key
     * @param time 设置过期时间类型为 秒
     * @return 该key对应的值是否失效 true为没有失效；false为失效
     */
    public boolean setExpire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key拿到key的失效时间
     *
     * @param key
     * @return 失效时间  秒  就是距离过期时间有多少秒
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断Key是否在redis中存在
     *
     * @param key 是
     * @return true存在，否则false
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存中所有的keys的值
     *
     * @param keys 可变长度的参数，可以是0个，1个和多个key进行删除
     */
    @SuppressWarnings("unchecked")
    public void delete(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(keys));
            }
        }
    }


    //////////////////////////String类型/////////////////////

    /**
     * 获取String类型的key对应的值
     *
     * @param key String类型redis数据的key
     * @return 该key对应的String的value值
     */
    public Object get(String key) throws Exception{
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 在redis服务器中是设置String类型的值
     *
     * @param key   String 类型的key
     * @param value 值
     * @return true 添加成功  否则 false
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在redis服务器中设置String类型的值，并设置失效时间
     * @param key String 类型的key
     * @param value 值
     * @param time 失效时间 秒
     * @return 设置成功返回true  否则false
     */
    public boolean set(String key, Object value, Long time) throws Exception{
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key,value);
            }
            return true;
    }


    /**
     * list集合
     * @param key
     * @param values
     * @return
     */
    public boolean listRightPush(String key, Object... values){
        Long pushAll = redisTemplate.opsForList().rightPushAll(key, values);
        if (pushAll > 0 ){
            return true;
        }

        return false;
    }

    /**
     * redis集合的获取
     * @param key
     * @return
     */
    public List<?> rangeList(String key){
        List<Object> list = redisTemplate.opsForList().range(key, 0, -1);
        return list;
    }





    /**
     * hash类型数据的存储
     * @param key hash 类型值的key
     * @param map 键值对
     * @return
     */
    public boolean hmset(String key, Map<String,Object> map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hash 类型数据存储
     * @param key hash类型的Key
     * @param map 键值对
     * @param time 失效时间
     * @return true设置成功，否则false
     */
    public boolean hmset(String key,Map<String,Object> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            if (time > 0){
                setExpire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 设置hash中指定key下的field的值为value
     * @param key  hash 的key建
     * @param field hash中的field域
     * @param value 给hash中的field设置的值
     * @return true设置成功，否则false
     */
    public boolean hset(String key,String field, Object value){
        try {
            redisTemplate.opsForHash().put(key,field,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 设置hash中指定key下field的值为value并设置失效时间
     * @param key hash的key
     * @param field hash的fieid
     * @param value 给hash中的key下的fieid 设置的值
     * @param time 失效时间
     * @return true设置成功 否则false
     */
    public boolean hset(String key,String field,Object value, long time){
        try {
            redisTemplate.opsForHash().put(key,field,value);
            if (time > 0){
                setExpire(key,time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取hash类型数据的key对应的整个map对象
     * @param key hash 中的Key
     * @param field key对应的hash对象
     * @return 该hash key 对应的hash对应
     */
    public Object hget(String key,String field){
        return redisTemplate.opsForHash().get(key,field);
    }


    /**
     * 获取hash类型数据的key对应的整个map对象
     * @param key hash 中的key
     * @return 该hash key对应的hash对象
     */
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 判断指定Key是否存在，存在则返回False，不存在则设置并返回True
     * @param key
     * @return
     */
    public Boolean stnx(String key){
        return redisTemplate.opsForValue().setIfAbsent(key, 0, Duration.ofMinutes(15));
    }
}
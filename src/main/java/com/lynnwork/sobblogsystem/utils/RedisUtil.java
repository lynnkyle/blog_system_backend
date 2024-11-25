package com.lynnwork.sobblogsystem.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*
        指定缓存失效时间
    */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        获取缓存过期时间
    */
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        判断缓存是否存在
    */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        删除缓存
    */
    public boolean del(String... key) {
        try {
            if (key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    List<String> keyList = Arrays.asList(key);
                    redisTemplate.delete(keyList);  // 传递 keyList 删除多个键
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        string缓存获取
    */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /*
        string缓存存储
    */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        string缓存存储
    */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        string缓存递增
    */
    public long incr(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        string缓存递减
    */

    public long decr(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        list缓存获取
    */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /*
        list缓存存储
    */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        list缓存存储
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        list缓存存储
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        list缓存存储
     */

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        list缓存索引取值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /*
        list缓存索引更新
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        list缓存删除
     */
    public boolean lRemove(String key, long count, Object value) {
        try {
            redisTemplate.opsForList().remove(key, count, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        list缓存长度
    */
    public long lSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        set缓存获取
    */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /*
        set缓存存储
    */
    public long sSet(String key, Object... value) {
        try {
            return redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        set缓存存储
    */
    public long sSet(String key, long time, Object... value) {
        try {
            long count = redisTemplate.opsForSet().add(key, value);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        set缓存查找value
    */
    public boolean sHasKey(String key, String value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        set缓存删除
    */
    public long sRemove(String key, Object... values) {
        try {
            long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        set缓存长度
    */
    public long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }

    /*
        hash缓存获取(单字段)
    */

    public Object hGet(String key, String field) {
        try {
            return redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /*
        hash缓存获取(所有字段)
    */

    public Map<Object, Object> hmGet(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /*
        hash缓存存储(单字段)
    */

    public boolean hSet(String key, String field, String value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
        hash缓存存储(单字段)
    */

    public boolean hSet(String key, String field, String value, long time) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
        hash缓存存储(多字段)
     */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        hash缓存存储(多字段)
    */
    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        hash缓存查找(单字段)
    */
    public boolean hHasKey(String key, String field) {
        try {
            return redisTemplate.opsForHash().hasKey(key, field);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        hash缓存自增(单字段)
    */
    public double hIncr(String key, String field, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, field, by);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1.0;
        }
    }

    /*
        hash缓存自减(单字段)
    */
    public double hDecr(String key, String field, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, field, -by);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1.0;
        }
    }

    /*
        hash缓存删除(单字段)
    */
    public boolean hDel(String key, String... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /*
        hash缓存长度
    */
    public long hSize(String key) {
        try {
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }
}

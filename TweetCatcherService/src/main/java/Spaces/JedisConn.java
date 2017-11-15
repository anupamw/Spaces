package Spaces;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class JedisConn {

    public Jedis jedis;

    public JedisConn() {
        final JedisPoolConfig poolConfig = buildPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost"); // Todo: on same vm for now
        this.jedis = jedisPool.getResource();
        try {
            // do operations with jedis resource
            this.jedis.lpush("names", "m fell");
            this.jedis.lpush("names", "juan mata");

        } finally {
            this.jedis.close();
        }
    }
    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }
}

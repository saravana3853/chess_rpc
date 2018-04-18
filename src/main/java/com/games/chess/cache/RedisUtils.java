package com.games.chess.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Tuple;

/**
 * 
 * @author AgarwalR
 * @since 22/02/2016 7:32PM IST
 *
 */
public class RedisUtils {

	public static boolean exists(String key) {
		Boolean exists = RedisClusterManager.getCluster().exists(key);
		return exists;
	}

	public static String set(String key, String value) {
		return RedisClusterManager.getCluster().set(key, value);
	}

	public static String set(byte[] key, byte[] value) {
		return RedisClusterManager.getCluster().set(key, value);
	}

	public static String get(String key) {
		return RedisClusterManager.getCluster().get(key);
	}

	public static byte[] get(byte[] key) {
		return RedisClusterManager.getCluster().get(key);
	}

	public static void hset(String key, String field, String value) {
		RedisClusterManager.getCluster().hset(key, field, value);
	}

	public static void hset(byte[] key, byte[] field, byte[] value) {
		RedisClusterManager.getCluster().hset(key, field, value);
	}

	public static boolean hexists(String key, String field) {
		return RedisClusterManager.getCluster().hexists(key, field);
	}

	public static String hget(String key, String field) {
		return RedisClusterManager.getCluster().hget(key, field);
	}

	public static void hdel(String key, String... fields) {
		RedisClusterManager.getCluster().hdel(key, fields);
	}

	public static void hdel(byte[] key, byte[] field) {
		RedisClusterManager.getCluster().hdel(key, field);
	}

	public static Map<String, String> hgetAll(String key) {
		return RedisClusterManager.getCluster().hgetAll(key);
	}

	public static void hdel(String key, String field) {
		RedisClusterManager.getCluster().hdel(key, field);
	}

	public static void del(String key) {
		RedisClusterManager.getCluster().del(key);
	}

	public static void del(byte[] key) {
		RedisClusterManager.getCluster().del(key);
	}

	public static String lpop(String key) {
		return lpop(key, false);
	}

	public static String lpop(String key, boolean isTxnSet) {
		return RedisClusterManager.getCluster().lpop(key);

	}

	public static void lpush(String key, String... items) {
		lpush(key, false, items);
	}

	public static void lpush(String key, boolean isTxnSet, String... items) {
		RedisClusterManager.getCluster().lpush(key, items);
	}

	public static String rpop(String key) {
		return RedisClusterManager.getCluster().rpop(key);
	}

	public static void rpush(String key, String... items) {
		rpush(key, false, items);
	}

	public static void rpush(String key, boolean isTxnSet, String... items) {
		RedisClusterManager.getCluster().rpush(key, items);
	}

	public static long sadd(String key, String member) {
		Long sadd = RedisClusterManager.getCluster().sadd(key, member);
		return sadd == null ? 0 : sadd;
	}

	public static void sadd(String key, String... values) {
		RedisClusterManager.getCluster().sadd(key, values);
	}

	public static void srem(String key, String member) {
		RedisClusterManager.getCluster().srem(key, member);
	}

	public static void srem(String key, String... values) {
		RedisClusterManager.getCluster().srem(key, values);
	}

	public static long scard(String key) {
		Long scard = RedisClusterManager.getCluster().scard(key);
		return scard == null ? 0 : scard;
	}

	public static Set<String> hkeys(String key) {
		return RedisClusterManager.getCluster().hkeys(key);
	}

	public static List<String> hvals(String key) {
		return RedisClusterManager.getCluster().hvals(key);
	}

	public static long hlen(String key) {
		Long hlen = RedisClusterManager.getCluster().hlen(key);
		return hlen == null ? 0 : hlen;
	}

	public static Set<String> smembers(String key) {
		return RedisClusterManager.getCluster().smembers(key);
	}

	public static String spop(String key) {
		return RedisClusterManager.getCluster().spop(key);
	}

	public static long incrBy(String key, long value) {
		return incrBy(key, value, false);
	}

	public static long incr(String key) {
		return incr(key, false);
	}

	public static long decr(String key) {
		return decr(key, false);
	}

	public static long incrBy(String key, long incrBy, boolean isTxnSet) {
		Long incrByRes = RedisClusterManager.getCluster().incrBy(key, incrBy);
		return incrByRes == null ? 0 : incrByRes;
	}

	public static long incr(String key, boolean isTxnSet) {
		Long incr = RedisClusterManager.getCluster().incr(key);
		return incr == null ? 0 : incr;

	}

	public static long decr(String key, boolean isTxnSet) {
		Long decr = RedisClusterManager.getCluster().decr(key);
		return decr == null ? 0 : decr;
	}

	public static String hmset(String key, Map<String, String> hash) {
		return RedisClusterManager.getCluster().hmset(key, hash);
	}

	public static List<String> hmget(String key, String[] fields) {
		return RedisClusterManager.getCluster().hmget(key, fields);
	}

	public static long llen(String key) {
		Long llen = RedisClusterManager.getCluster().llen(key);
		return llen == null ? 0 : llen;
	}

	public static List<String> lrange(String key, long start, long end) {
		return RedisClusterManager.getCluster().lrange(key, start, end);
	}

	public static void lrem(String key, long count, String value) {
		lrem(key, count, value, false);
	}

	public static void lrem(String key, long count, String item, boolean isTxnSet) {
		RedisClusterManager.getCluster().lrem(key, count, item);
	}

	public static String psetex(String key, long time, String value) {
		return RedisClusterManager.getCluster().psetex(key, time, value);
	}

	public static long pttl(String key) {
		Long pttl = RedisClusterManager.getCluster().pttl(key);
		return (pttl == null) || (pttl < 0) ? 0 : pttl;
	}

	public static boolean sismember(String key, String member) {
		Boolean sismember = RedisClusterManager.getCluster().sismember(key, member);
		return sismember == null ? false : sismember;
	}

	public static double zincrby(String key, int score, String member) {
		return zincrby(key, score, member, false);
	}

	public static double zincrby(String key, int score, String member, boolean isTxnSet) {

		Double zincrby = RedisClusterManager.getCluster().zincrby(key, score, member);
		return zincrby == null ? 0 : zincrby;

	}

	public static void zadd(String key, double score, String member) {
		RedisClusterManager.getCluster().zadd(key, score, member);
	}

	public static Set<Tuple> zRevRangeByScore(String key, long start, long end) {
		return RedisClusterManager.getCluster().zrevrangeWithScores(key, start, end);
	}

	public static Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return RedisClusterManager.getCluster().zrangeWithScores(key, start, end);
	}

	public static long zcard(String key) {
		Long zcard = RedisClusterManager.getCluster().zcard(key);
		return zcard == null ? 0 : zcard;
	}

	public static Set<String> zrange(String key, long start, long end) {
		return RedisClusterManager.getCluster().zrange(key, start, end);
	}

	public static Set<String> zrevrange(String key, long start, long end) {
		return RedisClusterManager.getCluster().zrevrange(key, start, end);
	}

	public static long zrank(String key, String member) {
		Long result = RedisClusterManager.getCluster().zrank(key, member);
		return result == null ? -1 : result;
	}

	public static long zrem(String key, String member) {
		Long result = RedisClusterManager.getCluster().zrem(key, member);
		return result == null ? 0 : result;
	}

	public static Set<String> zrangebyzcore(String key, String min, String max) {
		return RedisClusterManager.getCluster().zrangeByScore(key, min, max);
	}

	public static long zremrangeByScore(String key, String start, String end) {
		Long result = RedisClusterManager.getCluster().zremrangeByScore(key, start, end);
		return result == null ? 0 : result;
	}

	public static long zcount(String key, double start, double end) {
		Long result = RedisClusterManager.getCluster().zcount(key, start, end);
		return result == null ? -1 : result;
	}

	public static long zunionstore(String destKey, String... srcKey) {
		Long result = RedisClusterManager.getCluster().zunionstore(destKey, srcKey);
		return result == null ? -1 : result;
	}

	public static Object eval(String script, int keyCount, String... params) {
		return RedisClusterManager.getCluster().eval(script, keyCount, params);
	}

	public static Object evalsha(String sha1, int keyCount, String... params) {
		return RedisClusterManager.getCluster().evalsha(sha1, keyCount, params);
	}

	public static long sunionstore(String destKey, String... keys) {
		Long result = RedisClusterManager.getCluster().sunionstore(destKey, keys);
		return result == null ? -1 : result;
	}

	public static Set<String> sinter(String... keys) {
		return RedisClusterManager.getCluster().sinter(keys);
	}

	public static List<Map<String, String>> convertToMapList(Object seatListObj) {
		if (seatListObj == null)
			return null;

		List<Map<String, String>> resultList = new ArrayList<>();
		ArrayList<?> inputList = (ArrayList<?>) seatListObj;

		for (Object object : inputList) {
			if (object != null) {
				Map<String, String> detailsMap = new HashMap<String, String>();
				ArrayList<?> list = (ArrayList<?>) object;
				for (int i = 0; i < list.size(); i = i + 2) {
					if (list.get(i + 1) != null)
						detailsMap.put(list.get(i).toString(), list.get(i + 1).toString());
					else
						detailsMap.put(list.get(i).toString(), null);
				}
				resultList.add(detailsMap);
			}
		}
		return resultList;
	}

	public static Map<String, String> convertToMap(Object arrayObj) {
		if (arrayObj == null)
			return null;

		Map<String, String> resultList = new HashMap<String, String>();
		ArrayList<?> inputList = (ArrayList<?>) arrayObj;

		for (int i = 0; i < inputList.size(); i = i + 2) {
			resultList.put((String) inputList.get(i), (String) inputList.get(i + 1));
		}
		return resultList;
	}

}

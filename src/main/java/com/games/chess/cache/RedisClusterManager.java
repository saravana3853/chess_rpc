package com.games.chess.cache;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * 
 * @author AgarwalR
 * @since 22/02/2016 7:48PM IST
 *
 */
public class RedisClusterManager {

	private static JedisCluster jedisCluster;

	private RedisClusterManager() {
	}

	public static JedisCluster getCluster() {

		if (jedisCluster == null) {
			synchronized (RedisClusterManager.class) {
				if (jedisCluster == null) {
					Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
					String[] hosts = "localhost".split(",");
					String[] ports = "30001".split(",");

					for (int i = 0; i < ports.length; i++) {
						jedisClusterNodes.add(new HostAndPort(hosts[i], Integer.parseInt(ports[i])));
					}
					jedisCluster = new JedisCluster(jedisClusterNodes);

				}
			}
		}
		return jedisCluster;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Clone not supported for this class - " + RedisClusterManager.class);

	}

}

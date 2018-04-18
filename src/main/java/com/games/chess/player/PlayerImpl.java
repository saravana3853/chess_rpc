package com.games.chess.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.games.chess.cache.RedisUtils;
import com.games.chess.util.Deserializer;
import com.games.chess.util.Serializer;

public class PlayerImpl implements Player {

	public static final String key = "redis:player:";

	@Override
	public void add(PlayerDetails playerDetails) {
		if (playerDetails != null) {
			try {
				String playerKey = key + "{" + playerDetails.getUuid() + "}";
				RedisUtils.set(playerKey.getBytes(), Serializer.serialize(playerDetails));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public PlayerDetails get(String id) {
		String playerKey = key + "{" + id + "}";
		PlayerDetails details = null;
		try {
			byte[] bytes = RedisUtils.get(playerKey.getBytes());
			details = (PlayerDetails) Deserializer.deserialize(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;
	}

	@Override
	public boolean remove(String id) {
		return false;
	}

	@Override
	public void update(PlayerDetails playerDetails) {
		PlayerDetails details = get(playerDetails.getUuid());
		details.setColor(playerDetails.getColor());
		details.setBoardId(playerDetails.getBoardId());
		try {
			String playerKey = key + "{" + playerDetails.getUuid() + "}";
			RedisUtils.set(playerKey.getBytes(), Serializer.serialize(playerDetails));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

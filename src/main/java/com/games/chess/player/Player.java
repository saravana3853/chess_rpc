package com.games.chess.player;

public interface Player {

	void add(PlayerDetails playerDetails);

	PlayerDetails get(String id);

	void update(PlayerDetails playerDetails);

	boolean remove(String id);
}

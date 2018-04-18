package com.games.chess;

import java.util.List;

import com.games.chess.player.PlayerDetails;
import com.games.chess.server.ChessService;

public interface Chess {

	void play(PlayerDetails details);

	boolean isMyMove(String id);

	String[][] getBoard(String id);

	void move(String from, String to, String id);

	void print(String id);

	List<String> getPiecesRemoved(String id);

	void addCallBack(ChessService service);
}

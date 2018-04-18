package com.games.chess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.games.chess.board.COLOR;
import com.games.chess.board.ChessBoard;
import com.games.chess.player.PlayerDetails;

public class BoardPlayerDetails implements Serializable {

	private static final long serialVersionUID = 3071283442623582198L;

	ChessBoard board;
	Map<COLOR, PlayerDetails> map = new HashMap<>();

	public BoardPlayerDetails(ChessBoard board, Map<COLOR, PlayerDetails> details) {
		this.board = board;
		map.putAll(details);
	}

	public ChessBoard getBoard() {
		return board;
	}
}

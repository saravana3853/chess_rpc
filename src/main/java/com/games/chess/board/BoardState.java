package com.games.chess.board;

public enum BoardState {
	NORMAL, BLACK_CHECK, WHITE_CHECK, CHECKMATE, STALEMATE;

	@Override
	public String toString() {
		return this.name();
	}
}

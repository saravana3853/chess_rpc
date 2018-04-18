package com.games.chess.pieces;

import java.io.Serializable;

public interface IPiece extends Serializable {

	boolean checkValidMove(String from, String to);

	boolean isCheck();

	boolean isWhite();

	public int[][] getMoveMatrix();

	public boolean findCheckBreaker();
}

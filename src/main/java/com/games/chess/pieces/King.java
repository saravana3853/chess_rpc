package com.games.chess.pieces;

import java.util.List;

import com.games.chess.board.ChessBoard;
import com.games.chess.util.Util;

public class King extends Piece {

	int[][] moveMatrix = { { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { 1, 0 }, { -1, 0 }, { -1, 1 }, { -1, -1 } };

	public King(String name, ChessBoard chessBoard, boolean iswhite) {
		super(name, chessBoard, iswhite);
	}

	@Override
	public boolean checkValidMove(String from, String to) {

		List<Integer> pFrom = Util.getPosition(from, chessBoard);
		List<Integer> pTo = Util.getPosition(to, chessBoard);

		for (int i = 0; i < moveMatrix.length; i++) {
			int row = pFrom.get(0) + moveMatrix[i][0];
			int col = pFrom.get(1) + moveMatrix[i][1];

			if (pTo.get(0) == row && pTo.get(1) == col
					&& (chessBoard.getPiece(to) == null || chessBoard.getPiece(to).isWhite() != this.iswhite)) {
				return true;
			}
		}
		return false;
	}

	public int[][] getMoveMatrix() {
		return moveMatrix;
	}

	@Override
	public String toString() {
		String id = iswhite ? "0" : "1";
		return "K" + id;
	}

	@Override
	public boolean isCheck() {
		return false;
	}

	@Override
	public boolean findCheckBreaker() {
		List<Integer> pos = Util.getPosition(this, chessBoard);
		if (pos.isEmpty())
			return false;

		for (int i = 0; i < moveMatrix.length; i++) {
			int row = pos.get(0) + moveMatrix[i][0];
			int col = pos.get(1) + moveMatrix[i][1];

			if (checkBound(row, col)) {
				IPiece piece = chessBoard.getPiece(row, col);
				if (piece == null || piece.isWhite() != this.iswhite) {
					if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}

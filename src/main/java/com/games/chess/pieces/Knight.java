package com.games.chess.pieces;

import java.util.List;

import com.games.chess.board.ChessBoard;
import com.games.chess.util.Util;

public class Knight extends Piece {

	int[][] moveMatrix = { { 1, 2 }, { -1, 2 }, { 2, 1 }, { 2, -1 }, { 1, -2 }, { -1, -2 }, { -2, 1 }, { -2, -1 } };

	public Knight(String name, ChessBoard chessBoard, boolean iswhite) {
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

	@Override
	public String toString() {
		String id = iswhite ? "0" : "1";
		return "N" + id;
	}

	public int[][] getMoveMatrix() {
		return moveMatrix;
	}

	@Override
	public boolean isCheck() {
		List<Integer> pos = Util.getPosition(this, chessBoard);
		if (pos.isEmpty())
			return false;

		for (int i = 0; i < moveMatrix.length; i++) {
			int row = pos.get(0) + moveMatrix[i][0];
			int col = pos.get(1) + moveMatrix[i][1];

			if (checkBound(row, col) && chessBoard.getPiece(row, col) instanceof King
					&& ((Piece) chessBoard.getPiece(row, col)).iswhite != this.iswhite) {
				chessBoard.setCheckPiece(chessBoard.getPiece(row, col), this);
				return true;
			}
		}
		return false;
	}

	public boolean findCheckBreaker() {
		List<Integer> pos = Util.getPosition(this, chessBoard);
		if (pos.isEmpty())
			return false;

		for (int i = 0; i < moveMatrix.length; i++) {
			int row = pos.get(0) + moveMatrix[i][0];
			int col = pos.get(1) + moveMatrix[i][1];
			if (checkBound(row, col)) {
				if (chessBoard.getPiece(row, col) == null
						|| ((Piece) chessBoard.getPiece(row, col)).iswhite != this.iswhite) {
					if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
						return true;
					}
				}
			}
		}

		return false;
	}

}

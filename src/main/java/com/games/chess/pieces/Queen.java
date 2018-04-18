package com.games.chess.pieces;

import java.util.List;

import com.games.chess.board.ChessBoard;
import com.games.chess.util.Util;

public class Queen extends Piece {

	int[][] moveMatrix = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

	public Queen(String name, ChessBoard chessBoard, boolean iswhite) {
		super(name, chessBoard, iswhite);
	}

	@Override
	public boolean checkValidMove(String from, String to) {
		List<Integer> pFrom = Util.getPosition(from, chessBoard);
		List<Integer> pTo = Util.getPosition(to, chessBoard);

		for (int j = 0; j < moveMatrix.length; j++) {
			boolean piecebtw = false;
			for (int i = 0; i < chessBoard.getBoard().length; i++) {
				int row = pFrom.get(0) + (i * moveMatrix[j][0]);
				int col = pFrom.get(1) + (i * moveMatrix[j][1]);

				if (checkBound(row, col) && chessBoard.getBoard()[row][col] != null) {
					if (!(row == pFrom.get(0) && col == pFrom.get(1)) && !(row == pTo.get(0) && col == pTo.get(1))) {
						piecebtw = true;
					}
				}

				if (row == pTo.get(0) && col == pTo.get(1)
						&& (chessBoard.getPiece(to) == null || chessBoard.getPiece(to).isWhite() != this.iswhite)
						&& !piecebtw) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String id = iswhite ? "0" : "1";
		return "Q" + id;
	}

	@Override
	public boolean isCheck() {

		List<Integer> pos = Util.getPosition(this, chessBoard);
		if (pos.isEmpty())
			return false;

		for (int j = 0; j < moveMatrix.length; j++) {
			boolean piecebtw = false;
			for (int i = 0; i < chessBoard.getBoard().length; i++) {
				int row = pos.get(0) + (i * moveMatrix[j][0]);
				int col = pos.get(1) + (i * moveMatrix[j][1]);

				if (checkBound(row, col) && chessBoard.getBoard()[row][col] != null && !piecebtw) {
					IPiece piece = chessBoard.getPiece(row, col);
					if (piece instanceof King) {
						if (((Piece) piece).iswhite != this.iswhite) {
							chessBoard.setCheckPiece(piece, this);
							return true;
						}
					} else if (!(pos.get(0) == row && pos.get(1) == col)) {
						piecebtw = true;
					}
				}
			}
		}

		return false;
	}

	public boolean findCheckBreaker() {
		List<Integer> pos = Util.getPosition(this, chessBoard);
		if (pos.isEmpty())
			return false;

		for (int j = 0; j < moveMatrix.length; j++) {
			for (int i = 0; i < chessBoard.getBoard().length; i++) {
				int row = pos.get(0) + (i * moveMatrix[j][0]);
				int col = pos.get(1) + (i * moveMatrix[j][1]);
				if (checkBound(row, col)) {
					if (chessBoard.getPiece(row, col) == null || chessBoard.getPiece(row, col).isWhite() != isWhite()) {
						if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int[][] getMoveMatrix() {
		return moveMatrix;
	}
}

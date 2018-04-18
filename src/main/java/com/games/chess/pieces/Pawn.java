package com.games.chess.pieces;

import java.util.List;

import com.games.chess.board.ChessBoard;
import com.games.chess.util.Util;

public class Pawn extends Piece {

	int[][] moveMatrix = { { 1, 0 }, { 2, 0 }, { 1, 1 }, { 1, -1 } };

	boolean firstMove = true;

	public Pawn(String name, ChessBoard chessBoard, boolean iswhite) {
		super(name, chessBoard, iswhite);
	}

	@Override
	public boolean checkValidMove(String from, String to) {

		List<Integer> pFrom = Util.getPosition(from, chessBoard);
		List<Integer> pTo = Util.getPosition(to, chessBoard);
		if (iswhite) {
			return isWhitePawnMoveValid(to, pFrom, pTo);
		} else {
			return isBlackPawnMoveValid(to, pFrom, pTo);
		}
	}

	private boolean isBlackPawnMoveValid(String to, List<Integer> pFrom, List<Integer> pTo) {
		if (pFrom.get(0) - moveMatrix[0][0] == pTo.get(0) && pTo.get(1) == pFrom.get(1)
				&& chessBoard.getPiece(to) == null) {
			return true;
		}
		if (firstMove && pFrom.get(0) - moveMatrix[1][0] == pTo.get(0) && pTo.get(1) == pFrom.get(1)
				&& chessBoard.getPiece(to) == null) {
			firstMove = false;
			return true;
		}
		if (pFrom.get(0) - moveMatrix[2][0] == pTo.get(0) && pFrom.get(1) - moveMatrix[2][1] == pTo.get(1)
				&& (chessBoard.getPiece(to) != null && chessBoard.getPiece(to).isWhite() != this.iswhite)) {
			return true;
		}
		if (pFrom.get(0) - moveMatrix[3][0] == pTo.get(0) && pFrom.get(1) - moveMatrix[3][1] == pTo.get(1)
				&& (chessBoard.getPiece(to) != null && chessBoard.getPiece(to).isWhite() != this.iswhite)) {
			return true;
		}
		return false;
	}

	private boolean isWhitePawnMoveValid(String to, List<Integer> pFrom, List<Integer> pTo) {
		if (pFrom.get(0) + moveMatrix[0][0] == pTo.get(0) && pTo.get(1) == pFrom.get(1)
				&& chessBoard.getPiece(to) == null) {
			return true;
		}
		if (firstMove && pFrom.get(0) + moveMatrix[1][0] == pTo.get(0) && pTo.get(1) == pFrom.get(1)
				&& chessBoard.getPiece(to) == null) {
			firstMove = false;
			return true;
		}
		if (pFrom.get(0) + moveMatrix[2][0] == pTo.get(0) && pFrom.get(1) + moveMatrix[2][1] == pTo.get(1)
				&& (chessBoard.getPiece(to) != null && chessBoard.getPiece(to).isWhite() != this.iswhite)) {
			return true;
		}
		if (pFrom.get(0) + moveMatrix[3][0] == pTo.get(0) && pFrom.get(1) + moveMatrix[3][1] == pTo.get(1)
				&& (chessBoard.getPiece(to) != null && chessBoard.getPiece(to).isWhite() != this.iswhite)) {

			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String id = iswhite ? "0" : "1";
		return "P" + id;
	}

	public boolean findCheckBreaker() {

		List<Integer> pos = Util.getPosition(this, chessBoard);
		if (pos.isEmpty())
			return false;

		if (!iswhite)
			isBlackMoveBreaksCheck(pos);
		else
			isWhiteMoveBreaksCheck(pos);

		return false;
	}

	private boolean isWhiteMoveBreaksCheck(List<Integer> pos) {
		int row;
		int col;
		IPiece piece;
		row = pos.get(0) + moveMatrix[0][0];
		col = pos.get(1);
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
		}

		row = pos.get(0) + moveMatrix[1][0];
		col = pos.get(1);
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (firstMove && ((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			firstMove = false;
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
			firstMove = true;
		}

		row = pos.get(0) + moveMatrix[2][0];
		col = pos.get(1) + moveMatrix[2][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
		}

		row = pos.get(0) + moveMatrix[3][0];
		col = pos.get(1) + moveMatrix[3][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
		}

		return false;
	}

	private boolean isBlackMoveBreaksCheck(List<Integer> pos) {

		int row = pos.get(0) - moveMatrix[0][0];
		int col = pos.get(1);

		IPiece piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
		}

		row = pos.get(0) - moveMatrix[1][0];
		col = pos.get(1);
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (firstMove && ((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			firstMove = false;
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
			firstMove = true;
		}

		row = pos.get(0) - moveMatrix[2][0];
		col = pos.get(1) - moveMatrix[2][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
		}

		row = pos.get(0) - moveMatrix[3][0];
		col = pos.get(1) - moveMatrix[3][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (((piece != null && ((Piece) piece).iswhite != this.iswhite) || piece == null)) {
			if (assignAndCheck(pos.get(0), pos.get(1), row, col)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isCheck() {

		List<Integer> pos = Util.getPosition(this, chessBoard);

		if (pos.isEmpty())
			return false;

		int row = pos.get(0) - moveMatrix[2][0];
		int col = pos.get(1) - moveMatrix[2][1];

		IPiece piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (piece != null && piece instanceof King && ((Piece) piece).iswhite != this.iswhite) {
			chessBoard.setCheckPiece(piece, this);
			return true;
		}

		row = pos.get(0) - moveMatrix[3][0];
		col = pos.get(1) - moveMatrix[3][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (piece != null && piece instanceof King && ((Piece) piece).iswhite != this.iswhite) {
			chessBoard.setCheckPiece(piece, this);
			return true;
		}

		row = pos.get(0) + moveMatrix[2][0];
		col = pos.get(1) + moveMatrix[2][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (piece != null && piece instanceof King && ((Piece) piece).iswhite != this.iswhite) {
			chessBoard.setCheckPiece(piece, this);
			return true;
		}

		row = pos.get(0) + moveMatrix[3][0];
		col = pos.get(1) + moveMatrix[3][1];
		piece = checkBound(row, col) ? chessBoard.getPiece(row, col) : null;
		if (piece != null && piece instanceof King && ((Piece) piece).iswhite != this.iswhite) {
			chessBoard.setCheckPiece(piece, this);
			return true;
		}
		return false;
	}

	public int[][] getMoveMatrix() {
		return moveMatrix;
	}
}

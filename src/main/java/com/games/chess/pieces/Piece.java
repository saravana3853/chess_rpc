package com.games.chess.pieces;

import com.games.chess.board.ChessBoard;

public abstract class Piece implements IPiece {

	protected String name;
	protected ChessBoard chessBoard;
	protected boolean iswhite;

	public Piece(String name, ChessBoard chessBoard, boolean iswhite) {
		this.iswhite = iswhite;
		this.name = name;
		this.chessBoard = chessBoard;
	}

	public abstract boolean checkValidMove(String from, String to);

	public String getName() {
		return name;
	}

	public boolean isWhite() {
		return iswhite;
	}

	protected boolean checkBound(int row, int col) {
		return (row >= 0 && row < chessBoard.getBoard().length) && (col >= 0 && col < chessBoard.getBoard().length);
	}

	protected boolean assignAndCheck(int row, int col, int row1, int col1) {

		if (!(checkBound(row, col1) && checkBound(row1, col1))) {
			return false;
		}

		String oldId = chessBoard.getBoard()[row1][col1];
		chessBoard.getBoard()[row][col] = null;
		chessBoard.getBoard()[row1][col1] = this.name;
		if (!chessBoard.check()) {
			System.out.println("Move from " + row + "," + col + " to " + row1 + "," + col1 + "breaks check");
			chessBoard.getBoard()[row][col] = this.name;
			chessBoard.getBoard()[row1][col1] = oldId;
			return true;
		}
		chessBoard.getBoard()[row][col] = this.name;
		chessBoard.getBoard()[row1][col1] = oldId;
		return false;
	}

}
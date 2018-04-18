package com.games.chess.util;

import java.util.ArrayList;
import java.util.List;

import com.games.chess.board.ChessBoard;
import com.games.chess.pieces.IPiece;
import com.games.chess.pieces.Piece;

public class Util {

	public static List<Integer> getPosition(String from, ChessBoard chessboard) {
		List<Integer> list = new ArrayList<>();
		int row = chessboard.getRowVal(getString(from.toCharArray()[0]));
		int col = Integer.parseInt(getString(from.toCharArray()[1]));
		list.add(row);
		list.add(col);
		return list;
	}

	public static String getString(char c) {
		char[] ch = new char[] { c };
		return new String(ch);
	}

	public static String getValue(String from, ChessBoard chessboard, String[][] boardMatrix) {
		List<Integer> list = Util.getPosition(from, chessboard);
		return boardMatrix[list.get(0)][list.get(1)];
	}

	public static void setValue(String to, ChessBoard chessboard, String[][] boardMatrix, String uniqueId) {
		List<Integer> pos = Util.getPosition(to, chessboard);
		boardMatrix[pos.get(0)][pos.get(1)] = uniqueId;
	}

	public static List<Integer> getPosition(Piece piece, ChessBoard chessBoard) {

		List<Integer> list = new ArrayList<>();
		String[][] board = chessBoard.getBoard();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (piece.getName().equals(board[i][j])) {
					list.add(i);
					list.add(j);
				}
			}
		}
		return list;
	}
}

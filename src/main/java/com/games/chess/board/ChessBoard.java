package com.games.chess.board;

import java.io.Serializable;
import java.util.List;

import com.games.chess.pieces.IPiece;

public interface ChessBoard extends Serializable {

	boolean move(String from, String to);

	public int getRowVal(String row);

	public IPiece getPiece(String pos);

	public IPiece getPiece(int row, int col);

	public List<String> getPiecesRemoved();

	public void print(String id);

	public String[][] getPrintableBoard();

	public String[][] getBoard();

	public void setCheckPiece(IPiece king, IPiece checkBy);

	public BoardState getState();

	public boolean check();

	public boolean isWhite();
}

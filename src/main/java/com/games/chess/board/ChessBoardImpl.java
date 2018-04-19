package com.games.chess.board;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

import com.games.chess.pieces.Bishop;
import com.games.chess.pieces.IPiece;
import com.games.chess.pieces.King;
import com.games.chess.pieces.Knight;
import com.games.chess.pieces.Pawn;
import com.games.chess.pieces.Piece;
import com.games.chess.pieces.Queen;
import com.games.chess.pieces.Rook;
import com.games.chess.util.Util;

public class ChessBoardImpl implements ChessBoard {

	private static final long serialVersionUID = 9009654309458590094L;

	boolean white = true;

	BoardState state;

	King kingInCheck;

	IPiece checkBy;

	Stack<String> piecesRemoved = new Stack<>();

	String boardMatrix[][] = new String[8][8];

	Map<String, Integer> rowId = new HashMap<>();

	Map<String, IPiece> uuid = new HashMap<>();

	List<Class<? extends IPiece>> pieces = Arrays.asList(Rook.class, Knight.class, Bishop.class, Queen.class,
			King.class, Bishop.class, Knight.class, Rook.class);

	public ChessBoardImpl() {
		try {
			initBoard();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void print(String boardId) {
		for (int i = 0; i < boardMatrix.length; i++) {
			System.out.println("BoardId : " + boardId + " " + Arrays.asList(boardMatrix[i]).stream()
					.map(p -> uuid.get(p) == null ? "--" : uuid.get(p)).collect(Collectors.toList()));
		}
	}

	public String[][] getPrintableBoard() {
		String[][] brd = new String[boardMatrix.length][boardMatrix.length];
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix.length; j++) {
				if (uuid.get(boardMatrix[i][j]) != null)
					brd[i][j] = uuid.get(boardMatrix[i][j]).toString();
				else {
					brd[i][j] = "--";
				}
			}
		}

		return brd;
	}

	private void initBoard() throws Throwable {

		rowId = new HashMap<>();
		rowId.put("a", 0);
		rowId.put("b", 1);
		rowId.put("c", 2);
		rowId.put("d", 3);
		rowId.put("e", 4);
		rowId.put("f", 5);
		rowId.put("g", 6);
		rowId.put("h", 7);
		initializeWhite();
		initializeBlack();
		state = BoardState.NORMAL;
	}

	public void setState(BoardState state) {
		this.state = state;
	}

	private void initializeBlack()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		int i;
		i = 6;
		for (int j = 0; j < boardMatrix.length; j++) {
			String uniqueId = UUID.randomUUID().toString();
			IPiece chzpiece = new Pawn(uniqueId, this, false);
			uuid.put(uniqueId, chzpiece);
			boardMatrix[i][j] = uniqueId;
		}

		i = 7;
		for (int j = 0; j < boardMatrix.length; j++) {
			Class<? extends IPiece> klazz = pieces.get(j);
			String uniqueId = UUID.randomUUID().toString();
			IPiece chzpiece = klazz.getConstructor(String.class, ChessBoard.class, boolean.class).newInstance(uniqueId,
					this, false);
			uuid.put(uniqueId, chzpiece);
			boardMatrix[i][j] = uniqueId;
		}
	}

	private void initializeWhite()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		int i = 0;
		for (int j = 0; j < boardMatrix.length; j++) {
			Class<? extends IPiece> klazz = pieces.get(j);
			String uniqueId = UUID.randomUUID().toString();
			IPiece chzpiece = klazz.getConstructor(String.class, ChessBoard.class, boolean.class).newInstance(uniqueId,
					this, true);
			uuid.put(uniqueId, chzpiece);
			boardMatrix[i][j] = uniqueId;
		}

		i = 1;
		for (int j = 0; j < boardMatrix.length; j++) {
			String uniqueId = UUID.randomUUID().toString();
			IPiece chzpiece = new Pawn(uniqueId, this, true);
			uuid.put(uniqueId, chzpiece);
			boardMatrix[i][j] = uniqueId;
		}
	}

	public int getRowVal(String row) {
		return this.rowId.get(row);
	}

	public String[][] getBoard() {
		return boardMatrix;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

	public boolean isWhite() {
		return white;
	}

	@Override
	public boolean move(String from, String to) {
		String uniqueId = Util.getValue(from, this, boardMatrix);
		IPiece piece = uuid.get(uniqueId);
		if (getState().equals(BoardState.CHECKMATE)) {
			return false;
		}
		if (piece != null && isWhite() == piece.isWhite() && piece.checkValidMove(from, to)) {
			String oldId = Util.getValue(to, this, boardMatrix);
			Util.setValue(from, this, boardMatrix, null);
			Util.setValue(to, this, boardMatrix, uniqueId);
			becomeQueen(to, piece);
			if (oldId != null)
				piecesRemoved.push(uuid.get(oldId).toString());
			BoardState prev = getState();
			setState(BoardState.NORMAL);

			boolean isCheck = check();
			if (isCheck && kingInCheck != null && kingInCheck.isWhite() == piece.isWhite()) {
				// Move not valid
				Util.setValue(from, this, boardMatrix, uniqueId);
				Util.setValue(to, this, boardMatrix, oldId);
				if (oldId != null)
					piecesRemoved.pop();
				setState(prev);
				return false;
			} else if (isCheck && kingInCheck.isWhite() != piece.isWhite()) {
				if (kingInCheck.isWhite()) {
					setState(BoardState.WHITE_CHECK);
					System.out.println("White Check . ");
				} else {
					setState(BoardState.BLACK_CHECK);
					System.out.println("Black Check . ");
				}
				if (isCheckMated()) {
					System.out.println("Game Over. Winner is : " + COLOR.getColor(isWhite() ? 1 : 0));
					setState(BoardState.CHECKMATE);
					// System.exit(0);
				}
			} else if (!isCheck) {
				kingInCheck = null;
				checkBy = null;
			}

			if (isWhite())
				setWhite(false);
			else
				setWhite(true);

			return true;
		} else {
			System.out.println("Invalid Move : " + piece);
		}
		return false;
	}

	private void becomeQueen(String to, IPiece piece) {
		if (piece instanceof Pawn) {
			Integer rowPawn = Util.getPosition((Piece) piece, this).get(0);
			if ((rowPawn == 0 && !piece.isWhite()) || (rowPawn == 7 && piece.isWhite())) {
				System.out.println("Becoming queen ..");
				String id = UUID.randomUUID().toString();
				uuid.put(id, new Queen(id, this, piece.isWhite()));
				Util.setValue(to, this, boardMatrix, id);
			}
		}
	}

	public boolean isCheckMated() {
		boolean checkbreaker = uuid.values().stream().filter(p -> (p.isWhite() != isWhite()))
				.map(p -> p.findCheckBreaker()).reduce(Boolean::logicalOr).get();
		return !checkbreaker;
	}

	public boolean isStaleMated() {
		return false;
	}

	public boolean check() {
		boolean check = uuid.values().stream().map(p -> p.isCheck()).reduce(Boolean::logicalOr).get();
		return check;
	}

	@Override
	public IPiece getPiece(String pos) {
		List<Integer> list = Util.getPosition(pos, this);
		return uuid.get(boardMatrix[list.get(0)][list.get(1)]);
	}

	@Override
	public IPiece getPiece(int row, int col) {
		return uuid.get(boardMatrix[row][col]);
	}

	@Override
	public void setCheckPiece(IPiece king, IPiece checkBy) {
		if (king != null && (king instanceof King)) {
			kingInCheck = (King) king;
			this.checkBy = checkBy;
		}
	}

	@Override
	public BoardState getState() {
		return state;
	}

	@Override
	public List<String> getPiecesRemoved() {
		System.out.println("Removed Pieces :" + piecesRemoved);
		return new ArrayList<>(piecesRemoved);
	}

}

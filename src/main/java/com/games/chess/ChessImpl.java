package com.games.chess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.games.chess.board.COLOR;
import com.games.chess.board.ChessBoard;
import com.games.chess.board.ChessBoardImpl;
import com.games.chess.cache.RedisUtils;
import com.games.chess.player.Player;
import com.games.chess.player.PlayerDetails;
import com.games.chess.player.PlayerImpl;
import com.games.chess.server.ChessService;
import com.games.chess.util.Deserializer;
import com.games.chess.util.Serializer;

import io.grpc.stub.StreamObserver;

public class ChessImpl implements Chess {

	BlockingQueue<PlayerDetails> queue = new LinkedBlockingQueue<>();

	Player player = new PlayerImpl();

	List<ChessService> service = new ArrayList<>();

	public static final String boardKey = "redis:board:";

	public ChessImpl() {
		init();
		System.out.println("Initialized");
	}

	private void init() {
		new Thread() {
			@Override
			public void run() {
				List<PlayerDetails> details = new ArrayList<>();
				for (;;) {
					try {
						PlayerDetails take = queue.take();
						details.add(take);
						if (details.size() == 2) {
							Map<COLOR, PlayerDetails> map = new HashMap<>();
							map.put(COLOR.BLACK, details.get(0));
							map.put(COLOR.WHITE, details.get(1));
							ChessBoard chessBoard = new ChessBoardImpl();
							BoardPlayerDetails chessDetails = new BoardPlayerDetails(chessBoard, map);
							String id = UUID.randomUUID().toString();
							String key = boardKey + "{" + id + "}";
							RedisUtils.set(key.getBytes(), Serializer.serialize(chessDetails));
							updatePlayerDetails(details.get(0), COLOR.BLACK, id);
							updatePlayerDetails(details.get(1), COLOR.WHITE, id);
							System.out.println("Created Board : " + id);
							if (service.size() > 0) {
								for (ChessService chessService : service) {
									chessService.updateResponse(ChessService.PLAY_RESPONSE_TYPE,
											details.get(0).getUuid(), details.get(1).getUuid());
								}
							}
							details.clear();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();

	}

	private void updatePlayerDetails(PlayerDetails playerDetails, COLOR color, String uuid) {
		playerDetails.setBoardId(uuid);
		playerDetails.setColor(color);
		player.update(playerDetails);
	}

	@Override
	public void play(PlayerDetails details) {
		try {
			queue.put(details);
			player.add(details);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isMyMove(String id) {
		PlayerDetails playerDetails = player.get(id);
		BoardPlayerDetails boardPlayerDetails = getBoardDetails(playerDetails.getBoardId());
		boolean white = boardPlayerDetails != null ? boardPlayerDetails.getBoard().isWhite() : false;
		COLOR boardColor = white ? COLOR.WHITE : COLOR.BLACK;
		if (playerDetails != null && (boardPlayerDetails != null) && boardColor.equals(playerDetails.getColor())) {
			System.out.println(boardColor.name() + " " + playerDetails.getColor().name() + " " + "id : " + id);
			return true;
		}
		return false;
	}

	public BoardPlayerDetails getBoardDetails(String id) {
		BoardPlayerDetails boardPlayerDetails = null;
		String key = boardKey + "{" + id + "}";
		byte[] bytes = RedisUtils.get(key.getBytes());
		if (bytes != null) {
			try {
				boardPlayerDetails = (BoardPlayerDetails) Deserializer.deserialize(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return boardPlayerDetails;
	}

	@Override
	public String[][] getBoard(String id) {

		PlayerDetails playerDetails = player.get(id);
		BoardPlayerDetails boardPlayerDetails = getBoardDetails(playerDetails.getBoardId());
		if (boardPlayerDetails != null) {
			ChessBoard chessBoard = boardPlayerDetails.getBoard();
			return chessBoard.getPrintableBoard();
		}
		return null;
	}

	@Override
	public void move(String from, String to, String id) {
		PlayerDetails playerDetails = player.get(id);
		BoardPlayerDetails boardPlayerDetails = getBoardDetails(playerDetails.getBoardId());
		if (boardPlayerDetails != null) {
			ChessBoard board = boardPlayerDetails.getBoard();
			COLOR boardColor = board.isWhite() ? COLOR.WHITE : COLOR.BLACK;
			if (boardColor.equals(playerDetails.getColor())) {
				try {
					System.out.println(playerDetails.getBoardId() + "," + from + "," + to);
					board.move(from, to);
					String key = boardKey + "{" + playerDetails.getBoardId() + "}";
					RedisUtils.set(key.getBytes(), Serializer.serialize(boardPlayerDetails));
					print(id);
					for (ChessService chessService : service) {
						chessService.updateResponse(ChessService.PIECES_REMOVED_RESPONSE_TYPE, getPiecesRemoved(id),
								board.getState());
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void print(String id) {
		PlayerDetails playerDetails = player.get(id);
		BoardPlayerDetails boardPlayerDetails = getBoardDetails(playerDetails.getBoardId());
		if (boardPlayerDetails != null) {
			boardPlayerDetails.getBoard().print(playerDetails.getBoardId());
		}
	}

	@Override
	public void addCallBack(ChessService service) {
		this.service.add(service);
	}

	@Override
	public List<String> getPiecesRemoved(String id) {
		PlayerDetails playerDetails = player.get(id);
		BoardPlayerDetails boardPlayerDetails = getBoardDetails(playerDetails.getBoardId());
		if (boardPlayerDetails != null) {
			return boardPlayerDetails.getBoard().getPiecesRemoved();
		}

		return new ArrayList<>();
	}
}

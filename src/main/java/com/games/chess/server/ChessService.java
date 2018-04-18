package com.games.chess.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.games.chess.Chess;
import com.games.chess.ChessServiceGrpc.ChessServiceImplBase;
import com.games.chess.GetBoardRequest;
import com.games.chess.GetBoardResponse;
import com.games.chess.GetPiecesRemovedRequest;
import com.games.chess.GetPiecesRemovedResponse;
import com.games.chess.IsMyMoveRequest;
import com.games.chess.IsMyMoveResponse;
import com.games.chess.MoveRequest;
import com.games.chess.MoveResponse;
import com.games.chess.Piece;
import com.games.chess.PlayRequest;
import com.games.chess.PlayResponse;
import com.games.chess.Row;
import com.games.chess.board.BoardState;
import com.games.chess.client.GetPiecesRemovedObserver;
import com.games.chess.client.PlayResponseObserver;
import com.games.chess.player.PlayerDetails;

import io.grpc.stub.StreamObserver;

public class ChessService extends ChessServiceImplBase implements IChessService {

	public static final int PLAY_RESPONSE_TYPE = 1;
	public static final int PIECES_REMOVED_RESPONSE_TYPE = 2;

	public Map<String, StreamObserver<PlayResponse>> playResponses = new ConcurrentHashMap<>();
	public Map<String, StreamObserver<GetPiecesRemovedResponse>> removedPiecesResp = new ConcurrentHashMap<>();

	Chess chess;

	public ChessService(Chess chess) {
		this.chess = chess;
		this.chess.addCallBack(this);
	}

	@Override
	public void play(PlayRequest request, StreamObserver<PlayResponse> responseObserver) {
		PlayerDetails details = new PlayerDetails(request.getName(), request.getIp(), request.getUuid());
		playResponses.put(request.getUuid(), responseObserver);
		chess.play(details);
	}

	@Override
	public void move(MoveRequest request, StreamObserver<MoveResponse> responseObserver) {
		chess.move(request.getFrom(), request.getTo(), request.getId());
		MoveResponse response = MoveResponse.newBuilder().setMessage("Move made by client : " + request.getId())
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void isMyMove(IsMyMoveRequest request, StreamObserver<IsMyMoveResponse> responseObserver) {
		boolean ismove = chess.isMyMove(request.getId());
		IsMyMoveResponse response = IsMyMoveResponse.newBuilder().setVal(ismove).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void getPiecesRemoved(GetPiecesRemovedRequest request,
			StreamObserver<GetPiecesRemovedResponse> responseObserver) {
		removedPiecesResp.put(request.getId(), responseObserver);
	}

	@Override
	public void getBoard(GetBoardRequest request, StreamObserver<GetBoardResponse> responseObserver) {

		String[][] matrix = chess.getBoard(request.getId());
		System.out.println(request.getId());
		GetBoardResponse.Builder respBuilder = GetBoardResponse.newBuilder();
		for (int i = 0; i < matrix.length; i++) {
			Row.Builder rowBuilder = Row.newBuilder();
			for (int j = 0; j < matrix.length; j++) {
				rowBuilder.addPiece(matrix[i][j]);
			}
			respBuilder.addRow(rowBuilder);
		}
		responseObserver.onNext(respBuilder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void updateResponse(int responseType, Object... objects) {

		switch (responseType) {
		case PLAY_RESPONSE_TYPE: {
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] instanceof String) {
					StreamObserver<PlayResponse> stream = playResponses.get(objects[i]);
					if (stream != null) {
						stream.onNext(PlayResponse.newBuilder().setMessage("Board Created.").build());
						stream.onCompleted();
					}
				}
			}
		}
			break;

		case PIECES_REMOVED_RESPONSE_TYPE: {
			System.out.println(objects[0].toString() + " " + objects[1].toString());
			if (objects[0] instanceof List) {
				removedPiecesResp.values().stream().forEach(c -> {

					GetPiecesRemovedResponse.Builder builder = GetPiecesRemovedResponse.newBuilder();
					@SuppressWarnings("unchecked")
					List<String> object = (List<String>) objects[0];
					for (String pc : object) {
						Piece piece = Piece.newBuilder().setColor(pc.contains("1")).setName(pc).build();
						builder.addPieces(piece);
					}
					c.onNext(builder.build());

				});
			}

			if (BoardState.CHECKMATE.equals(objects[1])) {
				removedPiecesResp.values().stream().forEach(c -> {
					c.onCompleted();
				});
			}
		}
			break;
		}
	}
}

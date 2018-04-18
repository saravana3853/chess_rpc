package com.games.chess.client;

import java.util.ArrayList;
import java.util.List;

import com.games.chess.GetPiecesRemovedResponse;
import com.games.chess.Piece;

import io.grpc.stub.StreamObserver;

public class GetPiecesRemovedObserver implements StreamObserver<GetPiecesRemovedResponse> {

	List<String> pieces = new ArrayList<>();

	public GetPiecesRemovedObserver() {
	}

	@Override
	public void onNext(GetPiecesRemovedResponse value) {
		pieces.clear();
		List<Piece> piecesList = value.getPiecesList();
		for (Piece piece : piecesList) {
			pieces.add(piece.getName());
		}
		System.out.println("Added Pieces : " + pieces);
	}

	public List<String> getPieces() {
		return pieces;
	}

	@Override
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@Override
	public void onCompleted() {

	}

}

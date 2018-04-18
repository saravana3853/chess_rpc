package com.games.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import com.games.chess.client.ChessClient;
import com.games.chess.client.GetPiecesRemovedObserver;
import com.games.chess.client.PlayResponseObserver;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// ChessBoard chess = new ChessBoardImpl();
		List<String> list = new ArrayList<>();
		// Moves #1 checkmate and winner
		list.add("b4,c4");
		list.add("g0,f0");
		list.add("a3,c5");
		list.add("f0,e0");
		list.add("a5,d2");
		list.add("e0,d0");
		list.add("c5,g5");

		// Moves #3 finding checkmate
		// list.add("a6,c7");
		// list.add("g4,f4");
		// list.add("b1,c1");
		// list.add("h5,d2");
		// list.add("h5,e2");
		// list.add("c1,d1");
		// list.add("h3,f5");
		// list.add("d1,e2");
		// list.add("h6,f5");
		// list.add("h6,f7");
		// list.add("a2,c0");
		// list.add("f7,d6");
		// list.add("c0,e2");
		// list.add("e2,f2");
		// list.add("d6,b5");
		// list.add("a4,b5");
		// list.add("b2,d2");
		// list.add("b5,a7");
		// list.add("c0,d1");
		// list.add("f5,b5");

		// Moves #2 checkmate and winner
		// list.add("a6,c7");
		// list.add("g4,f4");
		// list.add("b1,c1");
		// list.add("h5,d2");
		// list.add("h5,e2");
		// list.add("c1,d1");
		// list.add("h3,f5");
		// list.add("d1,e2");
		// list.add("h6,f5");
		// list.add("h6,f7");
		// list.add("a2,c0");
		// list.add("f7,d6");
		// list.add("c0,e2");
		// list.add("e2,f2");
		// list.add("d6,b5");
		// list.add("a4,b5");
		// list.add("b2,d2");
		// list.add("b5,c7");
		// list.add("c0,d1");
		// list.add("f5,b5");

		List<ChessClient> clients = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			ChessClient client = ChessClient.getClient("localhost", 50051, UUID.randomUUID().toString(), "ip1");
			clients.add(client);
			if (clients.size() == 2) {

				final ChessClient client1 = clients.get(0);
				final ChessClient client2 = clients.get(1);
				final List<String> moves = new ArrayList<>(list);
				new Thread() {
					@Override
					public void run() {
						try {
							play(client1, client2, moves);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();

				clients.clear();
			}
		}

	}

	private static void play(ChessClient client1, ChessClient client2, List<String> list) throws InterruptedException {

		try {

			CountDownLatch latch = new CountDownLatch(2);
			PlayResponseObserver client1Observer = new PlayResponseObserver(latch);
			PlayResponseObserver client2Observer = new PlayResponseObserver(latch);

			GetPiecesRemovedObserver client1RemovedPieceObserver = new GetPiecesRemovedObserver();
			GetPiecesRemovedObserver client2RemovedPieceObserver = new GetPiecesRemovedObserver();

			client1.addRemovedPiecesObserver(client1RemovedPieceObserver);
			client2.addRemovedPiecesObserver(client2RemovedPieceObserver);

			client1.play(client1Observer);
			client2.play(client2Observer);

			latch.await();
			for (;;) {
				String val = list.remove(0);
				String[] moves = val.split("\\,");

				if (client1.isMyMove()) {
					System.out.println("Client : " + client1.getClientId() + " " + val);
					client1.move(moves[0], moves[1]);
					client1.printBoard();
					System.out.println("Removed Pieces : " + client1RemovedPieceObserver.getPieces());
				}

				Thread.sleep(2000);

				if (client2.isMyMove()) {
					System.out.println("Client : " + client2.getClientId() + " " + val);
					client2.move(moves[0], moves[1]);
					client2.printBoard();
					System.out.println("Removed Pieces : " + client2RemovedPieceObserver.getPieces());
				}

				if (list.size() <= 0) {
					break;
				}
			}

		} finally {
			client1.shutdown();
			client2.shutdown();
		}

	}

}

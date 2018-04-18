package com.games.chess.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.games.chess.ChessServiceGrpc;
import com.games.chess.GetBoardRequest;
import com.games.chess.GetBoardResponse;
import com.games.chess.GetPiecesRemovedRequest;
import com.games.chess.GetPiecesRemovedResponse;
import com.games.chess.IsMyMoveRequest;
import com.games.chess.IsMyMoveResponse;
import com.games.chess.MoveRequest;
import com.games.chess.MoveResponse;
import com.games.chess.PlayRequest;
import com.games.chess.PlayResponse;
import com.games.chess.Row;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.PickFirstBalancerFactory;
import io.grpc.stub.StreamObserver;
import io.grpc.util.RoundRobinLoadBalancerFactory;

public class ChessClient {

	private static final Logger logger = Logger.getLogger(ChessClient.class.getName());

	private final ManagedChannel channel;
	private final ChessServiceGrpc.ChessServiceBlockingStub blockingStub;
	private final ChessServiceGrpc.ChessServiceStub asyncStub;

	private String clientId;
	private String ip;

	List<URI> uris = new ArrayList<>();

	List<String> removedPieces = new ArrayList<>();

	{
		try {
			uris.add(new URI("local://localhost:50052/"));
			uris.add(new URI("local://localhost:50051/"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public ChessClient(String host, int port, String clientId, String ip) {
		channel = ManagedChannelBuilder.forTarget("local://localhost:500/")
				.nameResolverFactory(new AddressResolverProvider(uris)).idleTimeout(10000000, TimeUnit.SECONDS)
				.loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance()).usePlaintext(true).build();
		blockingStub = ChessServiceGrpc.newBlockingStub(channel);
		asyncStub = ChessServiceGrpc.newStub(channel);
		this.clientId = clientId;
		this.ip = ip;
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public String getClientId() {
		return clientId;
	}

	public String getIp() {
		return ip;
	}

	public void addRemovedPiecesObserver(StreamObserver<GetPiecesRemovedResponse> observer) {
		GetPiecesRemovedRequest req = GetPiecesRemovedRequest.newBuilder().setId(clientId).build();
		asyncStub.getPiecesRemoved(req, observer);
	}

	public void play(StreamObserver<PlayResponse> observer) {
		PlayRequest req = PlayRequest.newBuilder().setIp(ip).setName("plr" + clientId).setUuid(clientId).build();
		asyncStub.play(req, observer);
		// System.out.println(resp.getMessage());
	}

	public void move(String from, String to) {
		MoveRequest req = MoveRequest.newBuilder().setFrom(from).setTo(to).setId(clientId).build();
		MoveResponse resp = blockingStub.move(req);
		System.out.println(resp.getMessage());
	}

	public boolean isMyMove() {
		IsMyMoveRequest req = IsMyMoveRequest.newBuilder().setId(clientId).build();
		IsMyMoveResponse resp = blockingStub.isMyMove(req);
		return resp.getVal();
	}

	public void printBoard() {
		GetBoardRequest req = GetBoardRequest.newBuilder().setId(clientId).build();
		GetBoardResponse resp = blockingStub.getBoard(req);
		List<Row> rows = resp.getRowList();

		for (Row row : rows) {
			int pieceCount = row.getPieceCount();
			String[] arr = new String[pieceCount];
			for (int i = 0; i < pieceCount; i++) {
				arr[i] = row.getPiece(i);
			}
			System.out.println(Arrays.toString(arr));
		}
	}

	public static ChessClient getClient(String host, int port, String id, String ip) {
		ChessClient client = new ChessClient(host, port, id, ip);
		return client;
	}

	public static void main(String[] args) throws Throwable {

		ChessClient client = getClient("localhost", 8080, "x2", "ip");
		CountDownLatch latch = new CountDownLatch(1);
		client.play(new PlayResponseObserver(latch));
		GetPiecesRemovedObserver observer = new GetPiecesRemovedObserver();
		client.addRemovedPiecesObserver(observer);
		latch.await();

		for (;;) {

			if (client.isMyMove()) {
				System.out.println("Enter Move : ");
				Scanner scanner = new Scanner(System.in);
				String val = scanner.nextLine();
				String[] moves = val.split("\\,");
				if (moves.length == 2) {
					client.move(moves[0], moves[1]);
					client.printBoard();
					System.out.println("Removed chess pieces : " + observer.getPieces());
				}
			}
		}
	}
}

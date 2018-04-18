package com.games.chess.server;

import java.util.logging.Logger;

import com.games.chess.Chess;
import com.games.chess.ChessImpl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ChessServer {

	private static final Logger logger = Logger.getLogger(ChessServer.class.getName());

	/* The port on which the server should run */
	private int port = 50051;
	private Server server;

	private Chess chess = new ChessImpl();

	private void start() throws Exception {
		server = ServerBuilder.forPort(port).addService(new ChessService(chess)).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown
				// hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				ChessServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
	public static void main(String[] args) throws Exception {
		final ChessServer server = new ChessServer();
		server.start();
		server.blockUntilShutdown();
	}

}

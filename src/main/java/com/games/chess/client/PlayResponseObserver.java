package com.games.chess.client;

import java.util.concurrent.CountDownLatch;

import com.games.chess.PlayResponse;

import io.grpc.stub.StreamObserver;

public class PlayResponseObserver implements StreamObserver<PlayResponse> {

	CountDownLatch latch;

	public PlayResponseObserver(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void onNext(PlayResponse value) {
		System.out.println(value.getMessage());
	}

	@Override
	public void onError(Throwable t) {

	}

	@Override
	public void onCompleted() {
		latch.countDown();
	}

}

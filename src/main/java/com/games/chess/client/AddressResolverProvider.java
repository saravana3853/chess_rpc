package com.games.chess.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

public class AddressResolverProvider extends NameResolverProvider {

	List<URI> uris = new ArrayList<>();

	public AddressResolverProvider(List<URI> uris) {
		if (uris.isEmpty())
			new RuntimeException("URI list cannot be empty");
		this.uris.addAll(uris);
	}

	@Override
	protected boolean isAvailable() {
		return true;
	}

	@Override
	protected int priority() {
		return 5;
	}

	@Override
	public NameResolver newNameResolver(URI targetUri, Attributes params) {
		return new AddressResolver(uris);
	}

	@Override
	public String getDefaultScheme() {
		return uris.iterator().next().getScheme();
	}

}

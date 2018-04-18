package com.games.chess.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

public class AddressResolver extends NameResolver {

	List<URI> uris = new ArrayList<>();
	Listener listener;

	List<EquivalentAddressGroup> addressGrps = new ArrayList<>();

	public AddressResolver(List<URI> uris) {
		this.uris.addAll(uris);
		List<SocketAddress> socketAddress = new ArrayList<>();
		for (URI uri : uris) {
			System.out.println("URI : " + uri.getHost() + " Port :" + 50052);
			InetSocketAddress addr = new InetSocketAddress(uri.getHost(), uri.getPort());
			socketAddress.add(addr);
			addressGrps.add(new EquivalentAddressGroup(socketAddress));
		}
	}

	@Override
	public String getServiceAuthority() {
		return uris.iterator().next().getAuthority();
	}

	@Override
	public void start(Listener listener) {
		this.listener = listener;
		listener.onAddresses(addressGrps, Attributes.EMPTY);
	}

	@Override
	public void shutdown() {

	}

}

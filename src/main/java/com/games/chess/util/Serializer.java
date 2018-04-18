package com.games.chess.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer {

	public static byte[] serialize(Object object) throws IOException {
		if (object instanceof String)
			return ((String) object).getBytes();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(object);
		return byteArrayOutputStream.toByteArray();
	}
}

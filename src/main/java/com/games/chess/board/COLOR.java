package com.games.chess.board;

public enum COLOR {

	BLACK(0), WHITE(1);

	int id;

	COLOR(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static COLOR getColor(int id) {
		for (COLOR color : COLOR.values()) {
			if (color.getId() == id) {
				return color;
			}
		}
		return null;
	}
}
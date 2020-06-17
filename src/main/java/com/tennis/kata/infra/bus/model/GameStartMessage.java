package com.tennis.kata.infra.bus.model;

import lombok.Data;

@Data
public class GameStartMessage extends GameMessage {
	public static final String TYPE = "com.tennis.kata.game.start";

	public GameStartMessage() {
		super(TYPE);
	}
}

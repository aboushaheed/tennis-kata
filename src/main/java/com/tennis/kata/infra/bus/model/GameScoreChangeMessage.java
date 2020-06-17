package com.tennis.kata.infra.bus.model;

import lombok.Data;

@Data
public class GameScoreChangeMessage extends GameMessage {
	public static final String TYPE = "com.tennis.kata.game.scoreChange";

	public GameScoreChangeMessage() {
		super(TYPE);
	}
}

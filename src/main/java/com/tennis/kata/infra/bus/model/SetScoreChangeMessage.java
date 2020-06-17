package com.tennis.kata.infra.bus.model;

import lombok.Data;

@Data
public class SetScoreChangeMessage extends SetMessage {
	public static final String TYPE = "com.tennis.kata.game.setScoreChange";

	public SetScoreChangeMessage() {
		super(TYPE);
	}
}

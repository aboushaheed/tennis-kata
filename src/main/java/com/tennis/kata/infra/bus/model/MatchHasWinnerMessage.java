package com.tennis.kata.infra.bus.model;

import lombok.Data;

@Data
public class MatchHasWinnerMessage extends MatchWinnerMessage {
	public static final String TYPE = "com.tennis.kata.match.hasWinner";

	public MatchHasWinnerMessage() {
		super(TYPE);
	}
}

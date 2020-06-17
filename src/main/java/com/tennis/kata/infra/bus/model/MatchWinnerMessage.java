package com.tennis.kata.infra.bus.model;

import com.tennis.kata.domaine.game.model.Player;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MatchWinnerMessage extends MessageEvent {
	private MatchData match;

	public MatchWinnerMessage(String type) {
		super(type);
	}

	@Data
	public static class MatchData {
		private String matchName;
		private Player winner;
		private SetMessage.SetData set;
	}

}

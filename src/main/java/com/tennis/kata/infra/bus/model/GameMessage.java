package com.tennis.kata.infra.bus.model;

import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.Score;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

@Getter
@Setter
public abstract class GameMessage extends MessageEvent {
	private GameData game;

	public GameMessage(String type) {
		super(type);
	}

	@Data
	public static class GameData {
		private String matchName;
		private boolean douceMode;
		private Map<String, List<Score>> scoreBoard;
		private Player winner;
	}
}

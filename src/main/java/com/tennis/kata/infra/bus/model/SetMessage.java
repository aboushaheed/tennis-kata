package com.tennis.kata.infra.bus.model;

import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.SetScore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class SetMessage extends MessageEvent {
	private SetData set;

	public SetMessage(String type) {
		super(type);
	}

	@Data
	public static class SetData {
		private Map<String, List<SetScore>> scoreBoard;
		private List<GameMessage.GameData> games;
		private Player winner;
	}
}

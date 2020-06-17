package com.tennis.kata.interfaces.bus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MatchConfigAndStartEvent extends GameEvent {

	public static final String TYPE = "com.tennis.kata.match.config";
	private GameDTO game;

	@Data
	public static class GameDTO {
		private PlayerDTO playerOne;
		private PlayerDTO playerTwo;
		private String matchName;
		private boolean douceMode;
	}
	@Data
	public static class PlayerDTO {
		private String name;
	}
}

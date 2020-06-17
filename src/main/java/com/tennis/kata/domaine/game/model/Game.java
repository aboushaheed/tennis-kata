package com.tennis.kata.domaine.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.SortedSet;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Game {
	private String matchName;
	private boolean douceMode;
	private Map<Player, SortedSet<Score>> scoreBoard;
	private Player winner;

}

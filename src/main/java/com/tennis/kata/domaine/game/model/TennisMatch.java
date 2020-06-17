package com.tennis.kata.domaine.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TennisMatch {
	private String matchName;
	private TennisSet set;
	private Player winner;

}

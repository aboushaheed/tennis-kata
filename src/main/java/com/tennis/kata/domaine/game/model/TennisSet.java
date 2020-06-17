package com.tennis.kata.domaine.game.model;

import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.SetScore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TennisSet {
	private Map<Player, SortedSet<SetScore>> scoreBoard;
	private List<Game> games = new ArrayList<>();
	private Player winner;
	//private TieBreak tieBreak;
}

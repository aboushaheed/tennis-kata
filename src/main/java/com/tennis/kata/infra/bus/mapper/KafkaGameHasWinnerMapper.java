package com.tennis.kata.infra.bus.mapper;

import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.Score;
import com.tennis.kata.infra.bus.model.GameHasWinnerMessage;
import com.tennis.kata.infra.bus.model.GameMessage;
import com.tennis.kata.infra.bus.model.GameStartMessage;
import com.tennis.kata.infra.bus.model.GameWinnerMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.UUID;

@Service
public class KafkaGameHasWinnerMapper {


	public GameHasWinnerMessage mapHasWinnerMessage(Game game) {
		GameWinnerMessage.GameData winnerData = new GameWinnerMessage.GameData();
		Map<String, List<Score>> playerListMap = new HashMap<>();

		Map<Player, SortedSet<Score>> scoreBoard = game.getScoreBoard();
		scoreBoard.keySet().stream().forEach(player -> {
			playerListMap.put(player.getName(),new ArrayList<>(scoreBoard.get(player)));
		});
		winnerData.setScoreBoard(playerListMap);
		winnerData.setWinner(game.getWinner());

		winnerData.setMatchName(game.getMatchName());
		game.setDouceMode(game.isDouceMode());
		GameHasWinnerMessage gameHasWinnerMessage = new GameHasWinnerMessage();
		gameHasWinnerMessage.setId(UUID.randomUUID());
		gameHasWinnerMessage.setDate(LocalDateTime.now());
		gameHasWinnerMessage.setGame(winnerData);

		return gameHasWinnerMessage;
	}

}

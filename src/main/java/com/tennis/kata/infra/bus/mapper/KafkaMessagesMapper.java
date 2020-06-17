package com.tennis.kata.infra.bus.mapper;

import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.Score;
import com.tennis.kata.domaine.game.model.SetScore;
import com.tennis.kata.domaine.game.model.TennisSet;
import com.tennis.kata.infra.bus.model.GameMessage;
import com.tennis.kata.infra.bus.model.SetMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class KafkaMessagesMapper {

	public GameMessage.GameData toGameMessageGameData(Game game) {
		GameMessage.GameData gameData = new GameMessage.GameData();
		Map<String, List<Score>> playerListMap = new HashMap<>();

		Map<Player, SortedSet<Score>> scoreBoard = game.getScoreBoard();
		scoreBoard.keySet().stream().forEach(player -> {
			playerListMap.put(player.getName(),new ArrayList<>(scoreBoard.get(player)));
		});
		gameData.setScoreBoard(playerListMap);
		gameData.setWinner(game.getWinner());
		gameData.setMatchName(game.getMatchName());
		gameData.setDouceMode(game.isDouceMode());
		return gameData;
	}

	public SetMessage.SetData toSetMessageSetData(TennisSet tennisSet) {
		SetMessage.SetData setData = new SetMessage.SetData();
		Map<String, List<SetScore>> playerListMap;
		Map<Player, SortedSet<SetScore>> scoreBoard = tennisSet.getScoreBoard();
		playerListMap = scoreBoard.keySet().stream()
			.collect(toMap(Player::getName, player -> new ArrayList<>(scoreBoard.get(player)), (a, b) -> b));
		setData.setScoreBoard(playerListMap);
		setData.setWinner(tennisSet.getWinner());

		List<GameMessage.GameData> gameDataList = tennisSet.getGames().stream().map(game -> {
			GameMessage.GameData gameData = new GameMessage.GameData();
			gameData.setMatchName(game.getMatchName());
			Map<String, List<Score>> gameScores;
			Map<Player, SortedSet<Score>> gameScoreBoard = game.getScoreBoard();
			gameScores = gameScoreBoard.keySet().stream()
				.collect(toMap(Player::getName, player -> new ArrayList<>(gameScoreBoard.get(player)), (a, b) -> b));
			gameData.setScoreBoard(gameScores);
			gameData.setDouceMode(game.isDouceMode());
			gameData.setWinner(game.getWinner());
			return gameData;
		}).collect(toList());
		setData.setGames(gameDataList);
		setData.setWinner(tennisSet.getWinner());
		return setData;
	}
}

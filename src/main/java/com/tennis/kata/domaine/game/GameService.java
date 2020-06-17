package com.tennis.kata.domaine.game;

import com.tennis.kata.domaine.game.exception.PlayerNotFoundException;
import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.Score;
import com.tennis.kata.domaine.game.model.SetScore;
import com.tennis.kata.domaine.game.model.TennisMatch;
import com.tennis.kata.domaine.game.model.TennisSet;
import com.tennis.kata.infra.bus.PublisherService;
import com.tennis.kata.interfaces.bus.model.MatchConfigAndStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import static com.tennis.kata.domaine.game.model.Score.*;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.*;

@Service
@Slf4j
public class GameService {

	public static final int GAME_SPEED = 1000;
	public static final int DOUCE_STEP = 2;
	private PublisherService publisherService;

	@Autowired
	public GameService(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	public void play(MatchConfigAndStartEvent event) throws PlayerNotFoundException, InterruptedException {
		Game game = newGame(event);

		log.info("a new game is starting now : {}", game);
		publisherService.publishStartGame(game);
		TennisSet tennisSet = initTennisSet(game);

		do {
			do {
				game = pingPong(game);
				publisherService.publishGameScoreChange(game);
				MILLISECONDS.sleep(GAME_SPEED);

			} while (isNull(game.getWinner()));
			publisherService.publishGameHasWinner(game);
			log.info("the winner of this game is : {}", game.getWinner());

			increaseSet(game, tennisSet);
			publisherService.publishSetScoreChange(tennisSet);
			game = newGame(event);
		} while (isNull(tennisSet.getWinner()));

		TennisMatch tennisMatch = checkMatchWinner(game, tennisSet);
		publisherService.publishMatchHasWinner(tennisMatch);
	}

	public TennisMatch checkMatchWinner(Game game, TennisSet tennisSet) {
		Player playerOne = game.getScoreBoard().entrySet().stream().findFirst().get().getKey();
		SortedSet<SetScore> playerOneScore = tennisSet.getScoreBoard().get(playerOne);
		Player playerTwo = game.getScoreBoard().entrySet().stream().skip(1).findFirst().get().getKey();
		SortedSet<SetScore> playerTwoScore = tennisSet.getScoreBoard().get(playerTwo);

		TennisMatch tennisMatch = new TennisMatch();

		tennisMatch.setSet(tennisSet);
		tennisMatch.setMatchName(game.getMatchName());
		if (playerOneScore.last().getValue() > playerTwoScore.last().getValue()) {
			log.info("the winner of the match is {}", playerOne);
			tennisMatch.setWinner(playerOne);
		}
		if (playerOneScore.last().getValue() < playerTwoScore.last().getValue()) {
			log.info("the winner of the match is {}", playerTwo);
			tennisMatch.setWinner(playerTwo);
		}
		return tennisMatch;
	}

	public Game newGame(MatchConfigAndStartEvent event) {
		return Game.builder().douceMode(event.getGame().isDouceMode()).matchName(event.getGame().getMatchName()).scoreBoard(Map.of(
			Player.builder().name(event.getGame().getPlayerOne().getName()).build(), new TreeSet<>(Set.of(ZERO)),
			Player.builder().name(event.getGame().getPlayerTwo().getName()).build(), new TreeSet<>(Set.of(ZERO))))
			.build();
	}
	public Game pingPong(Game game) {
		Random random = new Random();

		if (random.nextBoolean()) {
			Optional<Player> first = game.getScoreBoard().keySet().stream().findFirst();
			if (first.isPresent()) {
				game = nextScore(game, first.get());
			}
		} else {
			Optional<Player> second = game.getScoreBoard().keySet().stream().skip(1).findAny();
			if (second.isPresent()) {
				game = nextScore(game, second.get());
			}
		}
		log.info("game score was changed : {}",game);
		return game;
	}


	public Game nextScore(Game game, Player player)  {

		if (isNull(game.getWinner())) {
			SortedSet<Score> playerGameScores =  game.getScoreBoard().get(player);
			Score playerLastScore = playerGameScores.last();

			if ( playerLastScore.equals(ADVANTAGE) || (playerLastScore.equals(FORTY) && !playerLastScore.isDouceAtive())) {
				game.setWinner(player);
			} else if (douceGame( game, player)) {
				addDouceScore(game);
			} else {
				playerGameScores.add(playerLastScore.increase());
				Map<Player, SortedSet<Score>> scoreBoard = new HashMap<>(game.getScoreBoard());
				scoreBoard.replace(player,playerGameScores);
				game.setScoreBoard(scoreBoard);

				if (cancelDouce(game)) {
					decreaseOtherPlayerScore(game, player);
				} else {
					game.getScoreBoard().entrySet().stream()
						.filter(o -> !o.getKey().equals(player))
						.forEach(o -> o.getValue().add(o.getValue().last()));
				}
			}
		}
		return game;
	}

	private void decreaseOtherPlayerScore(Game game, Player player) {
		game.getScoreBoard().entrySet().stream()
			.filter(o -> !o.getKey().equals(player))
			.forEach(o -> o.getValue().add(o.getValue().last().decrease()));
	}

	private void addDouceScore(Game game) {
		game.getScoreBoard().forEach((p, g) -> {
			Score douceScore = DEUCE;
			douceScore.setDouce(true);
			g.add(douceScore);
		});
	}

	private boolean cancelDouce(Game game) {
		Predicate<SortedSet<Score>> advantageOrDouce = g -> g.last().equals(ADVANTAGE) || g.last().equals(DEUCE);
		return BigDecimal.valueOf(game.getScoreBoard().values().stream().filter(advantageOrDouce).count()).intValue() == DOUCE_STEP;
	}


	private boolean douceGame(Game game, Player player) {
		SortedSet<Score> playerGameScores =  game.getScoreBoard().get(player);
		Score playerLastScore = playerGameScores.last();
		Score otherPlayerLastScore =
			game.getScoreBoard().entrySet().stream()
				.filter(o -> !o.getKey().equals(player))
				.map(Map.Entry::getValue).findFirst().map(SortedSet::last).orElse(null);
		assert otherPlayerLastScore != null;
		return playerLastScore.equals(FORTY) && otherPlayerLastScore.equals(ADVANTAGE);
	}



	public void increaseSet(Game game, TennisSet tennisSet) throws PlayerNotFoundException {
		List<Game> games = tennisSet.getGames();
		games.add(game);
		tennisSet.setGames(games);
		SortedSet<SetScore> winnerSetScores = ofNullable(tennisSet.getScoreBoard().get(game.getWinner()))
			.orElseThrow(() -> new PlayerNotFoundException(game.getWinner().getName()));

		Optional<SortedSet<SetScore>> looserSetScores = tennisSet.getScoreBoard().entrySet().stream()
			.filter(p-> !p.getKey().equals(game.getWinner())).map(Map.Entry::getValue).findFirst();

		SortedSet<SetScore> setScoresGameWinner = new TreeSet<>(winnerSetScores);
		SetScore lastSetScoreGameWinner = setScoresGameWinner.last();
		SetScore next = lastSetScoreGameWinner.next();
		SetScore lastSetScoreGameLooser = looserSetScores.map(SortedSet::last).orElse(null);

		assert lastSetScoreGameLooser != null;
		setScoresGameWinner.add(next);
		lastSetScoreGameWinner = setScoresGameWinner.last();

		if(lastSetScoreGameWinner.equals(SetScore.SIX) && lastSetScoreGameLooser.getValue() <= 4){
			tennisSet.setWinner(game.getWinner());
		}
		if(lastSetScoreGameWinner.equals(SetScore.SEVEN)) {
			tennisSet.setWinner(game.getWinner());
		}
		if(lastSetScoreGameWinner.getValue() >= 7 && lastSetScoreGameWinner.getValue() - lastSetScoreGameLooser.getValue() >= 2) {
				tennisSet.setWinner(game.getWinner());
		}


		Map<Player, SortedSet<SetScore>> scoreBoard = new HashMap<>(tennisSet.getScoreBoard());
		scoreBoard.replace(game.getWinner(),setScoresGameWinner);
		tennisSet.setScoreBoard(scoreBoard);
		log.info("set score was changed : {}",tennisSet);
	}

	public TennisSet initTennisSet(Game game) {
		TennisSet tennisSet = new TennisSet();
		Map<Player, SortedSet<SetScore>> setScoreBoard =  new HashMap<>();

		game.getScoreBoard().forEach((p, s) -> {
			SortedSet<SetScore> setScores = new TreeSet<>();
			setScores.add(SetScore.ZERO);
			setScoreBoard.put(p,setScores);
		});
		tennisSet.setScoreBoard(setScoreBoard);
		return tennisSet;
	}


}

package com.tennis.kata.domaine.game;

import com.tennis.kata.domaine.game.exception.PlayerNotFoundException;
import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.infra.bus.PublisherService;
import com.tennis.kata.interfaces.bus.model.MatchConfigAndStartEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.tennis.kata.domaine.game.model.Score.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GameServiceTest {

	private GameService gameService;

	@BeforeEach
	public void init() {
		PublisherService publisherService = mock(PublisherService.class);
		gameService = new GameService(publisherService);
	}

	@Test
	void should_increase_score_to_FIFTEEN_for_player_one() throws PlayerNotFoundException {
		Game game = getEmptyGame();
		Player playerOne = Player.builder().name("PlayerOne").build();
		game = gameService.nextScore(game, playerOne);

		Game playerOneWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
		assertEquals(playerOneWinPoint,game);
	}
	@Test
	void should_increase_score_to_THIRTY_for_player_one() throws PlayerNotFoundException {
		Game game = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
		Player playerOne = Player.builder().name("PlayerOne").build();
		game = gameService.nextScore(game, playerOne);

		Game playerOneWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(),  new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
		assertEquals(playerOneWinPoint,game);
	}
	@Test
	void should_increase_score_to_FORTY_for_player_one() throws PlayerNotFoundException {
		Game game = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
		Player playerOne = Player.builder().name("PlayerOne").build();
		game = gameService.nextScore(game, playerOne);

		Game playerOneWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY, FORTY)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
		assertEquals(playerOneWinPoint,game);
	}

	@Test
	void should_not_increase_score_for_player_one_when_he_has_ADVANTAGE() {
		Game game = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY,FORTY,ADVANTAGE)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.douceMode(true)
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.build();
		Player playerOne = Player.builder().name("PlayerOne").build();
		game = gameService.nextScore(game, playerOne);

		Game playerOneAdvantage = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY, FORTY,ADVANTAGE)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.douceMode(true)
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.build();
		assertEquals(playerOneAdvantage,game);
	}

	@Test
	void should_increase_score_to_FIFTEEN_for_player_two() throws PlayerNotFoundException {
		Game game = getEmptyGame();
		Player playerOne = Player.builder().name("PlayerTwo").build();
		game = gameService.nextScore(game, playerOne);

		Game playerTwoWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)))
			)
			.winner(null)
			.build();
		assertEquals(playerTwoWinPoint,game);
	}


	@Test
	void should_increase_score_to_THIRTY_for_player_two() throws PlayerNotFoundException {
		Game game = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)))
			)
			.winner(null)
			.build();
		Player playerTwo = Player.builder().name("PlayerTwo").build();
		game = gameService.nextScore(game, playerTwo);

		Game playerTwoWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(),  new TreeSet<>(Set.of(ZERO)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY)))
			)
			.winner(null)
			.build();
		assertEquals(playerTwoWinPoint,game);
	}
	@Test
	void should_increase_score_to_FORTY_for_player_two() {
		Game game = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY)))
			)
			.winner(null)
			.build();
		Player PlayerTwo = Player.builder().name("PlayerTwo").build();
		game = gameService.nextScore(game, PlayerTwo);

		Game playerTwoWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY, FORTY)))
			)
			.winner(null)
			.build();
		assertEquals(playerTwoWinPoint,game);
	}

	@Test
	void should_not_increase_score_for_player_two_when_he_has_ADVANTAGE() {
		Game game = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY,FORTY)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY,FORTY,ADVANTAGE)))
			)
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.build();
		Player PlayerTwo = Player.builder().name("PlayerTwo").build();
		game = gameService.nextScore(game, PlayerTwo);

		Game playerTwoAdvantage = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY, FORTY)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN, THIRTY, FORTY,ADVANTAGE)))
			)
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.build();
		assertEquals(playerTwoAdvantage,game);
	}



	@Test
	void should_play_a_game_score_will_change_for_a_player_randomly_for_the_first_shot() {
		//given
		Game start = getEmptyGame();
		//when
		Game firstShot = gameService.pingPong(start);

		//then
		Game playerOneWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
		Game playerTwoWinPoint = Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO, FIFTEEN)))
			)
			.winner(null)
			.build();

		assertTrue(playerTwoWinPoint.equals(firstShot) || playerOneWinPoint.equals(firstShot));

	}
	private Game getEmptyGame() {
		return  Game.builder()
			.scoreBoard(Map.of(
				Player.builder()
					.name("PlayerOne")
					.build(), new TreeSet<>(Set.of(ZERO)),
				Player.builder()
					.name("PlayerTwo")
					.build(), new TreeSet<>(Set.of(ZERO)))
			)
			.winner(null)
			.build();
	}


}
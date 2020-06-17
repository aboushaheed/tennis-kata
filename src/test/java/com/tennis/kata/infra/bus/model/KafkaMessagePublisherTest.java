package com.tennis.kata.infra.bus.model;

import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.Player;
import com.tennis.kata.domaine.game.model.Score;
import com.tennis.kata.domaine.game.model.SetScore;
import com.tennis.kata.domaine.game.model.TennisMatch;
import com.tennis.kata.domaine.game.model.TennisSet;
import com.tennis.kata.infra.bus.HelloMessage;
import com.tennis.kata.infra.bus.mapper.KafkaGameHasWinnerMapper;
import com.tennis.kata.infra.bus.mapper.KafkaGameScoreMapper;
import com.tennis.kata.infra.bus.mapper.KafkaGameStartMapper;
import com.tennis.kata.infra.bus.mapper.KafkaMatchHasWinnerMapper;
import com.tennis.kata.infra.bus.mapper.KafkaSetScoreMapper;
import com.tennis.kata.infra.bus.publisher.KafkaMessagePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaMessagePublisherTest {

	private KafkaMessagePublisher kafkaMessagePublisher;
	private KafkaGameStartMapper kafkaGameStartMapper;
	private MessageChannel messageChannel;
	private KafkaGameScoreMapper kafkaGameScoreMapper;
	private KafkaGameHasWinnerMapper kafkaGameHasWinnerMapper;
	private KafkaSetScoreMapper kafkaSetScoreMapper;

	private KafkaMatchHasWinnerMapper kafkaMatchHasWinnerMapper;

	@BeforeEach
	public void init() {
		Source source = mock(Source.class);
		messageChannel = mock(MessageChannel.class);
		kafkaGameStartMapper = mock(KafkaGameStartMapper.class);
		kafkaGameScoreMapper = mock(KafkaGameScoreMapper.class);
		kafkaGameHasWinnerMapper = mock(KafkaGameHasWinnerMapper.class);
		kafkaSetScoreMapper = mock(KafkaSetScoreMapper.class);
		when(source.output()).thenReturn(messageChannel);
		kafkaMatchHasWinnerMapper = mock(KafkaMatchHasWinnerMapper.class);
		kafkaMessagePublisher = new KafkaMessagePublisher(source,
			kafkaGameStartMapper, kafkaGameScoreMapper, kafkaGameHasWinnerMapper, kafkaSetScoreMapper, kafkaMatchHasWinnerMapper);
	}

	@Test
	public void should_call_source_when_publishing_hello() {
		//when
		kafkaMessagePublisher.publishHello();
		//then
		ArgumentCaptor<Message<MessageEvent>> evenementArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(messageChannel).send(evenementArgumentCaptor.capture());
		assertThat(evenementArgumentCaptor.getValue().getPayload()).isInstanceOf(HelloMessage.class);
	}


	@Test
	public void should_call_source_when_starting_a_game() {
		//given
		Game game = Game.builder()
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(Score.ZERO)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(Score.ZERO))))
			.build();
		GameStartMessage gameStartMessage = new GameStartMessage();
		GameMessage.GameData gameData = new GameMessage.GameData();
		gameData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(Score.ZERO)),
			"PlayerTwo", new ArrayList<>(Set.of(Score.ZERO))));
		gameStartMessage.setGame(gameData);
		when(kafkaGameStartMapper.mapStartMessage(game)).thenReturn(gameStartMessage);

		//WHEN
		kafkaMessagePublisher.publishStartGame(game);

		//THEN
		ArgumentCaptor<Message<MessageEvent>> evenementArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(messageChannel).send(evenementArgumentCaptor.capture());

		assertThat(evenementArgumentCaptor.getValue().getPayload()).isInstanceOf(GameStartMessage.class);
		assertThat(evenementArgumentCaptor.getValue().getPayload()).isSameAs(gameStartMessage);

	}
	/*
	void publishSetScoreChange( TennisSet tennisSet);
	 */
	@Test
	public void should_call_source_when_score_game_changed() {
		//given
		Game game = Game.builder()
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(Score.ZERO)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(Score.ZERO,Score.FIFTEEN))))
			.build();
		GameScoreChangeMessage gameScoreChangeMessage = new GameScoreChangeMessage();
		GameMessage.GameData gameData = new GameMessage.GameData();
		gameData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(Score.ZERO)),
			"PlayerTwo", new ArrayList<>(Set.of(Score.ZERO,Score.FIFTEEN))));
		gameScoreChangeMessage.setGame(gameData);
		when(kafkaGameScoreMapper.mapGameScoreChange(game)).thenReturn(gameScoreChangeMessage);

		//WHEN
		kafkaMessagePublisher.publishGameScoreChange(game);

		//THEN
		ArgumentCaptor<Message<MessageEvent>> evenementArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(messageChannel).send(evenementArgumentCaptor.capture());

		assertThat(evenementArgumentCaptor.getValue().getPayload()).isInstanceOf(GameScoreChangeMessage.class);
		assertThat(evenementArgumentCaptor.getValue().getPayload()).isSameAs(gameScoreChangeMessage);

	}
	@Test
	public void should_call_source_when_game_has_winner() {
		//given
		Game game = Game.builder()
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(Score.ZERO)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(Score.ZERO,Score.FIFTEEN,Score.THIRTY,Score.FORTY,Score.ADVANTAGE))))
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.build();
		GameHasWinnerMessage gameHasWinnerMessage = new GameHasWinnerMessage();
		GameWinnerMessage.GameData gameData = new GameWinnerMessage.GameData();
		gameData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(Score.ZERO)),
			"PlayerTwo", new ArrayList<>(Set.of(Score.ZERO,Score.FIFTEEN))));
		gameData.setWinner((Player.builder()
			.name("PlayerTwo")
			.build()));
		gameHasWinnerMessage.setGame(gameData);
		when(kafkaGameHasWinnerMapper.mapHasWinnerMessage(game)).thenReturn(gameHasWinnerMessage);

		//WHEN
		kafkaMessagePublisher.publishGameHasWinner(game);

		//THEN
		ArgumentCaptor<Message<MessageEvent>> evenementArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(messageChannel).send(evenementArgumentCaptor.capture());

		assertThat(evenementArgumentCaptor.getValue().getPayload()).isInstanceOf(GameHasWinnerMessage.class);
		assertThat(evenementArgumentCaptor.getValue().getPayload()).isSameAs(gameHasWinnerMessage);

	}
	@Test
	public void should_call_source_when_set_score_has_changed() {
		//given
		Game game = Game.builder()
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(Score.ZERO)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(Score.ZERO,Score.FIFTEEN,Score.THIRTY,Score.FORTY,Score.ADVANTAGE))))
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.matchName("match")
			.build();

		TennisSet tennisSet = TennisSet.builder()
			.games(List.of(game))
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(SetScore.ZERO,SetScore.ONE)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(SetScore.ZERO))))
			.build();

		SetScoreChangeMessage setScoreChangeMessage = new SetScoreChangeMessage();
		SetMessage.SetData data = new SetMessage.SetData();
		GameMessage.GameData gameData = new GameMessage.GameData();
		data.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(SetScore.ZERO,SetScore.ONE)),
			"PlayerTwo", new ArrayList<>(Set.of(SetScore.ZERO))));

		gameData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(Score.ZERO)),
			"PlayerTwo", new ArrayList<>(Set.of(Score.ZERO,Score.FIFTEEN,Score.THIRTY,Score.FORTY,Score.ADVANTAGE))));
		gameData.setMatchName("match");
		data.setGames(List.of(gameData));
		setScoreChangeMessage.setSet(data);

		when(kafkaSetScoreMapper.mapSetScoreChange(tennisSet)).thenReturn(setScoreChangeMessage);

		//WHEN
		kafkaMessagePublisher.publishSetScoreChange(tennisSet);

		//THEN
		ArgumentCaptor<Message<MessageEvent>> evenementArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(messageChannel).send(evenementArgumentCaptor.capture());

		assertThat(evenementArgumentCaptor.getValue().getPayload()).isInstanceOf(SetScoreChangeMessage.class);
		assertThat(evenementArgumentCaptor.getValue().getPayload()).isSameAs(setScoreChangeMessage);

	}
	@Test
	public void should_call_source_when_match_has_winner() {
		//given
		Game game = Game.builder()
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(Score.ZERO)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(Score.ZERO,Score.FIFTEEN,Score.THIRTY,Score.FORTY,Score.ADVANTAGE))))
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.matchName("match")
			.build();

		TennisSet tennisSet = TennisSet.builder()
			.games(List.of(game))
			.scoreBoard(Map.of(Player.builder()
				.name("PlayerOne")
				.build(), new TreeSet<>(Set.of(SetScore.ZERO,SetScore.ONE)),Player.builder()
				.name("PlayerTwo")
				.build(), new TreeSet<>(Set.of(SetScore.ZERO))))
			.build();

		TennisMatch tennisMatch = TennisMatch.builder()
			.winner(Player.builder()
				.name("PlayerTwo")
				.build())
			.set(tennisSet)
			.matchName(game.getMatchName())

			.build();


		MatchHasWinnerMessage matchHasWinnerMessage = new MatchHasWinnerMessage();
		MatchWinnerMessage.MatchData matchData = new MatchWinnerMessage.MatchData();
		matchData.setMatchName(game.getMatchName());
		matchData.setWinner(Player.builder()
			.name("PlayerTwo")
			.build());
		SetMessage.SetData setData = new SetMessage.SetData();
		setData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(SetScore.ZERO,SetScore.ONE)),
			"PlayerTwo", new ArrayList<>(Set.of(SetScore.ZERO))));
		matchData.setSet(setData);
		GameMessage.GameData gameData = new GameMessage.GameData();
		gameData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(Score.ZERO)),
			"PlayerTwo", new ArrayList<>(Set.of(Score.ZERO,Score.FIFTEEN))));
		gameData.setWinner(Player.builder()
			.name("PlayerTwo")
			.build());
		gameData.setScoreBoard(Map.of("PlayerOne", new ArrayList<>(Set.of(Score.ZERO)),
			"PlayerTwo", new ArrayList<>(Set.of(Score.ZERO,Score.FIFTEEN,Score.THIRTY,Score.FORTY,Score.ADVANTAGE))));
		gameData.setMatchName("match");
		setData.setGames(List.of(gameData));
		matchData.setSet(setData);
		matchHasWinnerMessage.setMatch(matchData);


		when(kafkaMatchHasWinnerMapper.mapMatchHasWinner(tennisMatch)).thenReturn(matchHasWinnerMessage);

		//WHEN
		kafkaMessagePublisher.publishMatchHasWinner(tennisMatch);

		//THEN
		ArgumentCaptor<Message<MessageEvent>> evenementArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(messageChannel).send(evenementArgumentCaptor.capture());

		assertThat(evenementArgumentCaptor.getValue().getPayload()).isInstanceOf(MatchHasWinnerMessage.class);
		assertThat(evenementArgumentCaptor.getValue().getPayload()).isSameAs(matchHasWinnerMessage);

	}
}
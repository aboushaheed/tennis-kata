package com.tennis.kata.infra.bus.publisher;

import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.TennisMatch;
import com.tennis.kata.domaine.game.model.TennisSet;
import com.tennis.kata.infra.bus.HelloMessage;
import com.tennis.kata.infra.bus.PublisherService;
import com.tennis.kata.infra.bus.mapper.KafkaGameHasWinnerMapper;
import com.tennis.kata.infra.bus.mapper.KafkaGameScoreMapper;
import com.tennis.kata.infra.bus.mapper.KafkaGameStartMapper;
import com.tennis.kata.infra.bus.mapper.KafkaMatchHasWinnerMapper;
import com.tennis.kata.infra.bus.mapper.KafkaSetScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
public class KafkaMessagePublisher implements PublisherService {

	private Source source;
	private KafkaGameStartMapper kafkaGameStartMapper;
	private KafkaGameScoreMapper kafkaGameScoreMapper;
	private KafkaGameHasWinnerMapper kafkaGameHasWinnerMapper;
	private KafkaSetScoreMapper kafkaSetScoreMapper;
	private KafkaMatchHasWinnerMapper kafkaMatchHasWinnerMapper;
	@Autowired
	public KafkaMessagePublisher(
		Source source, KafkaGameStartMapper kafkaGameStartMapper, KafkaGameScoreMapper kafkaGameScoreMapper, KafkaGameHasWinnerMapper kafkaGameHasWinnerMapper, KafkaSetScoreMapper kafkaSetScoreMapper,
		KafkaMatchHasWinnerMapper kafkaMatchHasWinnerMapper) {
		this.source = source;
		this.kafkaGameStartMapper = kafkaGameStartMapper;
		this.kafkaGameScoreMapper = kafkaGameScoreMapper;
		this.kafkaGameHasWinnerMapper = kafkaGameHasWinnerMapper;
		this.kafkaSetScoreMapper = kafkaSetScoreMapper;
		this.kafkaMatchHasWinnerMapper = kafkaMatchHasWinnerMapper;
	}

	@Override
	public void publishHello() {
		source.output().send(MessageBuilder.withPayload(new HelloMessage()).build());
	}

	@Override
	public void publishStartGame(Game game) {
		source.output().send(MessageBuilder.withPayload(kafkaGameStartMapper.mapStartMessage(game)).build());
	}

	@Override
	public void publishGameScoreChange(Game game) {
		source.output().send(MessageBuilder.withPayload(kafkaGameScoreMapper.mapGameScoreChange(game)).build());
	}

	@Override
	public void publishGameHasWinner(Game game) {
		source.output().send(MessageBuilder.withPayload(kafkaGameHasWinnerMapper.mapHasWinnerMessage(game)).build());
	}

	@Override
	public void publishSetScoreChange(TennisSet tennisSet) {
		source.output().send(MessageBuilder.withPayload(kafkaSetScoreMapper.mapSetScoreChange(tennisSet)).build());
	}

	@Override
	public void publishMatchHasWinner(TennisMatch tennisMatch) {
		source.output().send(MessageBuilder.withPayload(kafkaMatchHasWinnerMapper.mapMatchHasWinner(tennisMatch)).build());
	}

}

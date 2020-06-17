package com.tennis.kata.infra.bus.mapper;

import com.tennis.kata.domaine.game.model.TennisSet;
import com.tennis.kata.infra.bus.model.SetMessage;
import com.tennis.kata.infra.bus.model.SetScoreChangeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.*;
import static java.util.stream.Collectors.toMap;
@Service
public class KafkaSetScoreMapper {

	private KafkaMessagesMapper kafkaMessagesMapper;

	@Autowired
	public KafkaSetScoreMapper(KafkaMessagesMapper kafkaMessagesMapper) {
		this.kafkaMessagesMapper = kafkaMessagesMapper;
	}

	public SetScoreChangeMessage mapSetScoreChange(TennisSet tennisSet) {

		assert nonNull(tennisSet);

		SetMessage.SetData setData = kafkaMessagesMapper.toSetMessageSetData(tennisSet);

		SetScoreChangeMessage setScoreChangeMessage = new SetScoreChangeMessage();
		setScoreChangeMessage.setId(UUID.randomUUID());
		setScoreChangeMessage.setDate(LocalDateTime.now());
		setScoreChangeMessage.setSet(setData);

		return setScoreChangeMessage;

	}


}

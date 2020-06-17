package com.tennis.kata.infra.bus.mapper;

import com.tennis.kata.domaine.game.model.TennisMatch;
import com.tennis.kata.domaine.game.model.TennisSet;
import com.tennis.kata.infra.bus.model.MatchHasWinnerMessage;
import com.tennis.kata.infra.bus.model.MatchWinnerMessage;
import com.tennis.kata.infra.bus.model.SetMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class KafkaMatchHasWinnerMapper {

	private KafkaMessagesMapper kafkaMessagesMapper;

	@Autowired
	public KafkaMatchHasWinnerMapper(KafkaMessagesMapper kafkaMessagesMapper) {
		this.kafkaMessagesMapper = kafkaMessagesMapper;
	}

	public MatchHasWinnerMessage mapMatchHasWinner(TennisMatch tennisMatch) {

		MatchWinnerMessage.MatchData matchData = new MatchWinnerMessage.MatchData();

		matchData.setMatchName(tennisMatch.getMatchName());
		matchData.setSet(kafkaMessagesMapper.toSetMessageSetData(tennisMatch.getSet()));
		matchData.setWinner(tennisMatch.getWinner());


		MatchHasWinnerMessage matchHasWinnerMessage = new MatchHasWinnerMessage();
		matchHasWinnerMessage.setId(UUID.randomUUID());
		matchHasWinnerMessage.setDate(LocalDateTime.now());
		matchHasWinnerMessage.setMatch(matchData);

			return matchHasWinnerMessage;
	}
}

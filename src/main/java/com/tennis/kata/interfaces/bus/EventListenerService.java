package com.tennis.kata.interfaces.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennis.kata.domaine.game.GameService;
import com.tennis.kata.domaine.game.exception.PlayerNotFoundException;
import com.tennis.kata.interfaces.bus.model.GameEvent;
import com.tennis.kata.interfaces.bus.model.MatchConfigAndStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EventListenerService {

	private ObjectMapper mapper;
	private GameService gameService;

	public EventListenerService(ObjectMapper mapper, GameService gameService) {
		this.mapper = mapper;
		this.gameService = gameService;
	}

	public void handleEvent(String context) {
		log.info(context);
		try {
			processEvent(context);
		} catch (Exception e) {
			log.error("Can not process this event", e);
		}
	}


	private void processEvent(String context) throws IOException, PlayerNotFoundException, InterruptedException {
		GameEvent evenement = mapper.readValue(context, GameEvent.class);
		if (MatchConfigAndStartEvent.TYPE.equals(evenement.getType())) {
			handleStartGameEvent(context);
		}
	}

	private void handleStartGameEvent(String context) throws IOException, PlayerNotFoundException, InterruptedException {
		MatchConfigAndStartEvent matchConfigAndStartEvent = mapper.readValue(context, MatchConfigAndStartEvent.class);
		gameService.play(matchConfigAndStartEvent);

	}

}

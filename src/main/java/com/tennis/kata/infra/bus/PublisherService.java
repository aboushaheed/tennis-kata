package com.tennis.kata.infra.bus;

import com.tennis.kata.domaine.game.model.Game;
import com.tennis.kata.domaine.game.model.TennisMatch;
import com.tennis.kata.domaine.game.model.TennisSet;

public interface PublisherService {

	void publishHello();
	void publishStartGame( Game game);
	void publishGameScoreChange( Game game);
	void publishGameHasWinner( Game game);
	void publishSetScoreChange( TennisSet tennisSet);
	void publishMatchHasWinner(TennisMatch tennisMatch);
}

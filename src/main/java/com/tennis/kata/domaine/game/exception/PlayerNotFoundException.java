package com.tennis.kata.domaine.game.exception;

import java.util.function.Supplier;

public class PlayerNotFoundException extends Exception  {

	public PlayerNotFoundException(String name) {
		super(String.format("the player %s is not found ",name));
	}
}

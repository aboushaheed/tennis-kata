package com.tennis.kata.interfaces.bus.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public  class GameEvent {

	private UUID id;
	private UUID parentId;
	private LocalDateTime date;
	private String type;
}

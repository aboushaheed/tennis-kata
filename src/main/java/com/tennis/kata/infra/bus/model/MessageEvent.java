package com.tennis.kata.infra.bus.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MessageEvent {
	private UUID id = UUID.randomUUID();
	private UUID parentId;
	private LocalDateTime date = LocalDateTime.now();
	private final String type;

	public MessageEvent(String type) {
		this.type = type;
	}
}

package com.tennis.kata.infra.bus;

import com.tennis.kata.infra.bus.model.GameMessage;
import lombok.Data;

import java.util.UUID;

@Data
public class HelloMessage  extends GameMessage {
	public static final String TYPE = "com.tennis.kata.hello";

	private UUID traceId;

	public HelloMessage() {
		super(TYPE);
		setTraceId(getId());
	}

}

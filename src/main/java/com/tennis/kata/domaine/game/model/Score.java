package com.tennis.kata.domaine.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Score {
	ZERO("0"),
	FIFTEEN("15"),
	THIRTY("30"),
	FORTY("40"),
	ADVANTAGE("ADVANTAGE"),
	DEUCE("DEUCE");
	private boolean isDeuce;

	public Score increase() {
			if(isDeuce) {
				return this.equals(FORTY) || this.equals(DEUCE) ? ADVANTAGE : this;
			} else {
				return (ordinal() == values().length - 1 || this.equals(Score.ADVANTAGE) || this.equals(Score.DEUCE))
					? this : values()[ordinal() + 1];
			}
	}
	public Score decrease() {
		if (isDeuce) {
			return this.equals(DEUCE) ? FORTY : this;
		} else {
			return ordinal() == 0 || this.equals(ADVANTAGE) || this.equals(DEUCE) ? this : values()[ordinal() - 1];
		}
	}
 public void setDouce(boolean douce) {
		isDeuce = douce;
 }
 public boolean isDouceAtive() {
	 return isDeuce;
 }
	Score(String value) {
	}
}
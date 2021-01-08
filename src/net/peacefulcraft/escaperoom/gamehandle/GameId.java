package net.peacefulcraft.escaperoom.gamehandle;

import java.util.UUID;

public class GameId extends Object {
	private UUID owner;
		public UUID getOwner() { return this.owner; }

	private String escapeRoomName;
		public String getEscapeRoomName() { return this.escapeRoomName; }

	private Long startTimeSysMils;
		public Long getStartTimeMills() { return this.startTimeSysMils; }

	public String getGameId() { return this.owner.toString() + "_" + this.escapeRoomName.replace(" ", "") + "_" + this.startTimeSysMils; }

	public GameId(UUID owner, String escapeRoomName, Long startTimeSysMils) {
		this.owner = owner;
		this.escapeRoomName = escapeRoomName;
		this.startTimeSysMils = startTimeSysMils;
	}

	public String toString() { return this.getGameId(); }
}

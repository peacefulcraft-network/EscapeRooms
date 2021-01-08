package net.peacefulcraft.escaperoom.gamehandle;

import net.peacefulcraft.escaperoom.config.EscapeRoomConfiguration;

public class EscapeRoomGame {
	private EscapeRoomConfiguration config;
		public EscapeRoomConfiguration getConfig() { return this.config; }

	private Boolean isActiveGame;
		public Boolean isActiveGame() { return this.isActiveGame; }

	private GameSession gameSession;
		public GameSession getGameSession() { return this.gameSession; }

	private GameState gameState;
		public GameState getGameState() { return this.gameState; }

	public EscapeRoomGame(EscapeRoomConfiguration config) {
		this.config = config;
		this.isActiveGame = false;
	}

	public EscapeRoomGame(GameSession gameSession, EscapeRoomConfiguration config) {
		this.gameSession = gameSession;
		this.config = config;
		this.isActiveGame = true;
	}
}

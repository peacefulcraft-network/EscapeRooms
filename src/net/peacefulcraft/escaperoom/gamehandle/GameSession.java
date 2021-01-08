package net.peacefulcraft.escaperoom.gamehandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

public class GameSession {
	private GameId gameId;
		public GameId getGameId() { return this.gameId; }

	private EscapeRoomGame escapeRoom;
		public EscapeRoomGame getEscapeRoomGame() { return this.escapeRoom; }

	private Player sessionOwner;
		public Player getSessionOwner() { return this.sessionOwner; }

	private List<Player> players;
		public List<Player> getPlayers() { return Collections.unmodifiableList(this.players); }

	public GameSession(EscapeRoomGame escapeRoom, Player sessionOwner) {
		this.gameId = new GameId(sessionOwner.getUniqueId(), escapeRoom.getConfig().getName(), System.currentTimeMillis());
		this.escapeRoom = escapeRoom;
		this.sessionOwner = sessionOwner;

		this.players = new ArrayList<Player>();
		this.players.add(sessionOwner);
	}
}

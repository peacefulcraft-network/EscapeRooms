package net.peacefulcraft.escaperoom.gamehandle;

public enum GameState {
	/* Preparing world for players. Players are in general lobby */
	DEPLOY,

	/* Waiting for all players to join. Players are in rule room */
	PRE_GAME,
	
	/* All players have left. Game will remain for 5 minutes before timing out */
	IDLE,
	
	/* Game is active. Players are playing and in the world */
	PLAYING,
	
	/* Game has ended in a loss. Players are in loss room, slowly leaving */
	LOSS,
	
	/* Game has ended in a win. Players are in victory room, slowly leaving */
	WIN,
	
	/* All players have left. Game is being cleaned up */
	CLEANUP
}

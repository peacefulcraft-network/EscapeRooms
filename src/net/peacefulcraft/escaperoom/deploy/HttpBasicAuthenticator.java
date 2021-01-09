package net.peacefulcraft.escaperoom.deploy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class HttpBasicAuthenticator extends Authenticator {
	private final PasswordAuthentication auth;
	
	public HttpBasicAuthenticator(String username, String password) {
		this.auth = new PasswordAuthentication(username, password.toCharArray());
	}

	protected PasswordAuthentication getPasswordAuthentication() { return this.auth; }
}

package net.peacefulcraft.escaperoom.deploy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import net.peacefulcraft.escaperoom.EscapeRoom;

public class AuthenticatedHttpsFileDownload {

	private String authString;

	public AuthenticatedHttpsFileDownload(String username, String password) {
		this.authString = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Downloads the requested file from the specified location (Assumes HTTP with Basic Authentication, but theoretically works with any streamable input)
	 * @param url_s Url to the desired file
	 * @param saveLoc Path to the location where the file should be saved
	 * @return An MD5 hex string for the downloaded file
	 */
	public String download(String url_s, String saveLoc) throws RuntimeException {
		EscapeRoom._this().logNotice("Opening connection to file for download " + url_s);
		try {
			URL url = new URL(url_s);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Basic " + this.authString);
			con.setRequestProperty("User-Agent", "PeacefulCraft Bukkit Server/EscapeRooms");
			File saveLocFile = new File(saveLoc); 
			MessageDigest hash = MessageDigest.getInstance("MD5");
			try (
				BufferedInputStream in = new BufferedInputStream(con.getInputStream());
				FileOutputStream fout = new FileOutputStream(saveLocFile);
			) {
				byte data[] = new byte[8000];
				int readIn;
				while((readIn = in.read(data, 0, 8000)) != -1) {
					fout.write(data, 0, readIn);
					hash.update(data, 0, readIn);
				}

				String hexHash = DeploymentPackage.MD5DigestToHexString(hash); 
				EscapeRoom._this().logNotice("Downloaded file " + saveLoc + " md5(" + hexHash + ")");
				return hexHash;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("An error occured while downloading the requested resource. Check console for more details.");
		} catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			EscapeRoom._this().logSevere("Your system does not suppor the MD5 hashing algorithm. Please report this to the developers.");
			EscapeRoom._this().logSevere("Canceling requested deployment");
			return null;
		}
	}
}

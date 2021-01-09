package net.peacefulcraft.escaperoom.deploy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import net.peacefulcraft.escaperoom.EscapeRoom;

/*
 * Reference
 * https://mail.codejava.net/java-se/networking/upload-files-by-sending-multipart-request-programmatically
 */
public class AuthenticatedHttpsMultipartForm {
	private final String lineEnding = "\r\n"; 
	private String boundry;
	private HttpsURLConnection con;
	private OutputStream os;
	private PrintWriter pw;

	public AuthenticatedHttpsMultipartForm(String url_s, String username, String password) {
		this.boundry = "===t5839wg**(&$@Wajfd80oj$===";

		try {
			EscapeRoom._this().logDebug("Opening connection to URL " + url_s);
			URL url = new URL(url_s);
			String authString = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

			this.con = (HttpsURLConnection) url.openConnection();
			this.con.setRequestMethod("POST");
			this.con.setDoOutput(true);
			this.con.setRequestProperty("Authorization", "Basic " + authString);
			this.con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundry);
			this.con.setRequestProperty("User-Agent", "PeacefulCraft Bukkit Server/EscapeRooms");
			this.os = this.con.getOutputStream();
			this.pw = new PrintWriter(new OutputStreamWriter(this.os, "UTF-8"));

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to open HTTPs Connection. See console for details.");
		}
	}

	/**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        this.pw.append("--" + this.boundry).append(this.lineEnding);
        this.pw.append("Content-Disposition: form-data; name=\"" + name + "\"").append(this.lineEnding);
		this.pw.append("Content-Type: text/plain; charset=" + this.lineEnding).append(this.lineEnding);
		this.pw.append(lineEnding);
		this.pw.append(value).append(this.lineEnding);
		this.pw.flush();
		EscapeRoom._this().logDebug("Added field to AuthenticatedMultipartForm: " + name);
	}
	
	    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
		String fileName = uploadFile.getName();
        this.pw.append("--" + this.boundry).append(this.lineEnding);
        this.pw.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(this.lineEnding);
        this.pw.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(this.lineEnding);
        this.pw.append("Content-Transfer-Encoding: binary").append(this.lineEnding);
        this.pw.append(this.lineEnding);
        this.pw.flush();
 
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            this.os.write(buffer, 0, bytesRead);
        }
        this.os.flush();
        inputStream.close();
         
        this.pw.append(this.lineEnding);
		this.pw.flush();
		
		EscapeRoom._this().logDebug("Added file to AuthenticatedMultipartForm: " + fileName);
	}
	
	/**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
		List<String> response = new ArrayList<String>();
		EscapeRoom._this().logDebug("Sending AuthenticatedMultipartForm to server");
 
        this.pw.append(this.lineEnding).flush();
        this.pw.append("--" + this.boundry + "--").append(this.lineEnding);
        this.pw.close();
 
        // checks server's status code first
        int status = this.con.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
			this.con.disconnect();
			EscapeRoom._this().logDebug("AuthenticatedMultipartForm response ok.");
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
 
        return response;
    }
}

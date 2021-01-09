package net.peacefulcraft.escaperoom.deploy;

import java.io.IOException;

import net.peacefulcraft.escaperoom.config.DeploymentProviderConfiguration;

public class DeploymentProvider {

	private DeploymentProviderConfiguration config;

	public DeploymentProviderConfiguration getConfig() {
		return this.config;
	}

	public DeploymentProvider(DeploymentProviderConfiguration config) {
		this.config = config;
	}

	public void push(DeploymentPackage pack) throws RuntimeException{
		try {
			AuthenticatedHttpsMultipartForm mpf = new AuthenticatedHttpsMultipartForm(this.config.getDeploymentUploadUrl(),
				this.config.getDeploymentUser(), this.config.getDeploymentPassword()
			);

			mpf.addFilePart("file", pack.getPackagedZipFile());
			mpf.finish();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error uploading file to deployment site. Check console for more details");
		}
	}

	/**
	 * Downloads the requested file from the specified location (Assumes HTTP with Basic Authentication, but theoretically works with any streamable input)
	 * @param url_s Url to the desired file
	 * @param saveLoc Path to the location where the file should be saved
	 * @return An MD5 hex string for the downloaded file
	 */
	public String pull(String url_s, String saveLoc) throws RuntimeException {
		AuthenticatedHttpsFileDownload downloadClient = new AuthenticatedHttpsFileDownload(this.config.getDeploymentUser(), this.config.getDeploymentPassword());
		return downloadClient.download(url_s, saveLoc);
	}
}

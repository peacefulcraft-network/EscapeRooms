package net.peacefulcraft.escaperoom.config;

public class DeploymentProviderConfiguration extends Configuration {
	public DeploymentProviderConfiguration() {
		super("deploy.yml", "deploy.yml");

		this.loadValues();
	}

	private void loadValues() {
		this.deploymentManifestUrl = this.config.getString("deployment_manifest_url");
		this.deploymentUploadUrl = this.config.getString("deployment_upload_url");
		this.deploymentDownloadUrl = this.config.getString("deployment_download_url");
		this.deploymentUser = this.config.getString("deployment_user");
		this.deploymentPassword = this.config.getString("deployment_password").trim();
	}

	private String deploymentManifestUrl;
		public String getDeploymentManifestUrl() { return this.deploymentManifestUrl; }

	private String deploymentUploadUrl;
		public String getDeploymentUploadUrl() { return this.deploymentUploadUrl; }

	private String deploymentDownloadUrl;
		public String getDeploymentDownloadUrl() { return this.deploymentDownloadUrl; }

	private String deploymentUser;
		public String getDeploymentUser() { return this.deploymentUser; }

	private String deploymentPassword;
		public String getDeploymentPassword() { return this.deploymentPassword; }
}

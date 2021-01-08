package net.peacefulcraft.escaperoom.config;

public class DeploymentProviderConfiguration extends Configuration {
	public DeploymentProviderConfiguration() {
		super("deploy.yml", "deploy.yml");

		this.loadValues();
	}

	private void loadValues() {
		this.deploymentUrl = this.config.getString("deployment_url");
		this.deploymentUser = this.config.getString("deployment_user");
		this.deploymentPassword = this.config.getString("deployment_password");
	}

	private String deploymentUrl;
		public String getDeploymentUrl() { return this.deploymentUrl; }

	private String deploymentUser;
		public String getDeploymentUser() { return this.deploymentUser; }

	private String deploymentPassword;
		public String deploymentPassword() { return this.deploymentPassword; }
}

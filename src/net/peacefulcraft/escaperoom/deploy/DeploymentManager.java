package net.peacefulcraft.escaperoom.deploy;

public class DeploymentManager {
	
	private DeploymentProvider deploymentProvider;
		public DeploymentProvider getDeploymentProvider() { return this.deploymentProvider; }

	public DeploymentManager(DeploymentProvider deploymentProvider) {
		this.deploymentProvider = deploymentProvider;
	}
}

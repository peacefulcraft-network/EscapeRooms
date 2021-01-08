package net.peacefulcraft.escaperoom.deploy;

import net.peacefulcraft.escaperoom.config.DeploymentProviderConfiguration;

public class DeploymentProvider {
	
	private DeploymentProviderConfiguration config;
		public DeploymentProviderConfiguration getConfig() { return this.config; }

	public DeploymentProvider(DeploymentProviderConfiguration config) {
		this.config = config;
	}
}

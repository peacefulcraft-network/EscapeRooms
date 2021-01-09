package net.peacefulcraft.escaperoom.deploy;

import org.bukkit.configuration.ConfigurationSection;

import net.peacefulcraft.escaperoom.EscapeRoom;
import net.peacefulcraft.escaperoom.config.Configuration;
import net.peacefulcraft.escaperoom.config.DeploymentProviderConfiguration;

public class DeploymentManifest extends Configuration {
	
	public DeploymentManifest() {
		super("manifest.yml", "packages/manifest.yml");

		DeploymentProviderConfiguration config = EscapeRoom._this().getDeploymentManager().getDeploymentProvider().getConfig();
		AuthenticatedHttpsFileDownload downloadClient = new AuthenticatedHttpsFileDownload(config.getDeploymentUser(), config.getDeploymentPassword());
		downloadClient.download(config.getDeploymentManifestUrl(), this.configFile.toString());
		this.loadConfiguration();
	}

	public void addDeploymentPackage(DeploymentPackage pack) {
		String worldName = pack.getName().replaceAll(" ", "_");
		ConfigurationSection section = this.config.createSection(worldName);
		section.set("name", pack.getName());
		section.set("world_name", worldName);
	}

	public void getDeploymentPackage(String name) {

	}
}

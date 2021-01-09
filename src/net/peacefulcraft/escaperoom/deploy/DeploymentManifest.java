package net.peacefulcraft.escaperoom.deploy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import net.peacefulcraft.escaperoom.config.Configuration;

public class DeploymentManifest extends Configuration {
	
	public DeploymentManifest() {
		super("manifest.yml", "packages/manifest.yml");

		this.loadConfiguration();
	}

	public void setDeploymentPackage(DeploymentPackage pack) {
		String packageName = this.getPackageName(pack.getName());
		ConfigurationSection section = this.config.createSection(packageName);
		section.set("name", pack.getName());
		section.set("hash", pack.getPackageHash());
		this.saveConfiguration();
	}

	public Map<String, DeploymentPackage> getDeploymentPackages() {
		Set<String> keys = this.config.getKeys(false);
		Map<String, DeploymentPackage> depPackages = new HashMap<String, DeploymentPackage>();

		keys.forEach((packageName) -> {
			ConfigurationSection section = this.config.getConfigurationSection(packageName);
			depPackages.put(
				section.getString("name"),
				new DeploymentPackage(section.getString("name"), section.getString("hash"))
			);
		});

		return depPackages;
	}

	public void removeDeploymentPackage(String name) {
		this.config.set(this.getPackageName(name), null);
		this.saveConfiguration();
	}

	private String getPackageName(String name) {
		return name.replaceAll(" ", "_");
	}
}

package net.peacefulcraft.escaperoom.deploy;

import java.util.Collections;
import java.util.Map;

import net.peacefulcraft.escaperoom.EscapeRoom;

public class DeploymentManager {

	private Map<String, DeploymentPackage> deploymentPackages;
		public DeploymentPackage getDeploymentPackage(String name) { return this.deploymentPackages.get(name); }
		public Map<String, DeploymentPackage> getDeploymentPackages() { return Collections.unmodifiableMap(this.deploymentPackages); }

	private DeploymentProvider deploymentProvider;
		public DeploymentProvider getDeploymentProvider() { return this.deploymentProvider; }

	private DeploymentManifest deploymentManifest;
		public DeploymentManifest getDeploymentManifest() { return this.deploymentManifest; }

	public DeploymentManager(DeploymentProvider deploymentProvider) {
		this.deploymentProvider = deploymentProvider;

		if (EscapeRoom._this().getConfiguration().getServerMode() == ServerMode.PRODUCTION) {
			// Only pull the manifest if this server is in production.
			this.deploymentManifest = this.deploymentProvider.pullDeploymentManifest();
		} else {
			// Otherwise we want to use our local version incase we've staged changes
			this.deploymentManifest = new DeploymentManifest();
		}

		this.deploymentPackages = this.deploymentManifest.getDeploymentPackages();
	}

	/**
	 * Package an EsapeRoom for deploymnet. Overrides existing package if one exists
	 * @param name The name of the EsapeRoom to pacakge
	 * @return The DeploymentPackage with references to appropriate resources on the disk.
	 */
	public void packageForDeployment(String name) throws RuntimeException {
		DeploymentPackage newPackage = new DeploymentPackage(name);
		newPackage.boxup(); // throws RuntimeException
		this.deploymentPackages.put(name, newPackage);
		this.deploymentManifest.setDeploymentPackage(newPackage);
	}

	public void shipDeploymentPackage(String name) throws RuntimeException {
		DeploymentPackage targetPackage = this.deploymentPackages.get(name);
		if (targetPackage == null) {
			throw new RuntimeException("Unable to deploy requested package " + name + ". Has the package been created yet?");
		}

		// Shouldn't ever happen, but check anyway incase we change how this work later
		if (targetPackage.isVolitile()) {
			throw new RuntimeException("Blocking deployment of package " + name + ". Package is flagged as volitile and is likley corrupt.");
		}

		if (targetPackage.isCanonical()) {
			EscapeRoom._this().logNotice("Skipping deploy of package " + name + " as it is still marked as canonical.");
			return;
		}

		this.deploymentProvider.push(targetPackage); // Can throw RuntimeException
		EscapeRoom._this().logNotice("Shipped package " + targetPackage.getName() + ". Hash: " + targetPackage.getPackageHash());
	}

	public void pullDeploymentPackage(String name) throws RuntimeException {
		DeploymentPackage pack = this.deploymentPackages.get(name);
		if (pack == null) {
			throw new RuntimeException("Unable to pull requested package " + name + ". Is this package name correct? Is the manifest up to date?");
		}
		
		String downloadUrl = this.deploymentProvider.getConfig().getDeploymentDownloadUrl() + pack.getName().replaceAll(" ", "_") + ".zip";
		this.deploymentProvider.pull(downloadUrl, pack.getPackagedZipFile().toString());
	}

	public void unpackDeploymentPackage(String name) throws RuntimeException {
		DeploymentPackage pack = this.deploymentPackages.get(name);
		if (pack == null) {
			throw new RuntimeException("Unable tp unpack requested package " + name + ". Is this package name correct? Is the manifest up to date?");
		}
		pack.unpack();
	}
}

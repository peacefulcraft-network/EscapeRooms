package net.peacefulcraft.escaperoom.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.peacefulcraft.escaperoom.EscapeRoom;

public class DeploymentPackage {

	private String name;
		public String getName() { return this.name; }

	private String packageHash;
		public String getPackageHash() { return this.packageHash; }

	// Package is being modifiy or was modified and did not complete sucesfully. Package should not be deployed while in this state
	private Boolean isVolitile;
		public Boolean isVolitile() { return this.isVolitile; }

	private Boolean isCanonical;
		public Boolean isCanonical() { return this.isCanonical; }

	private File configFile;
		public File getConifgFile() { return this.configFile; }

	private File worldFolder;
		public File getWorldFolder() { return this.worldFolder; }

	private File packagedZipFile;
		public File getPackagedZipFile() { return this.packagedZipFile; }

	public DeploymentPackage(String name) {
		this.name = name;
		this.isCanonical = false;
		this.isVolitile = false;
		this.configFile = new File(EscapeRoom._this().getDataFolder(), "/escaperooms/" + name + ".yml");
		this.worldFolder = new File(EscapeRoom._this().getDataFolder().getParentFile().getParentFile(),
				name.replaceAll(" ", "_"));
		this.packagedZipFile = new File(EscapeRoom._this().getDataFolder(), "/packages/" + name + ".zip");
	}

	public DeploymentPackage(String name, String packageHash) {
		this(name);
		this.packageHash = packageHash;
		this.isVolitile = false;
		this.isCanonical = true;
	}

	/**
	 * Creates a zip file with the world and configuration file for this deploymnet
	 * Updatse the hash property on this object that respresents the hash for the
	 * deployment zip
	 * 
	 * @throws RuntimeException
	 * @return An MD5 hash of the zip'd package or null if the system doesn't support MD5 hashes
	 */
	public String boxup() throws RuntimeException {
		if (EscapeRoom._this().getServer().getWorld(this.worldFolder.getName()) != null) {
			throw new RuntimeException("Unable to package world for deploy while it is loaded. Please unload it first.");
		}

		// Create new, empty file to to populate w/ package
		try {
			this.packagedZipFile.delete();
			this.isCanonical = false;
			this.isVolitile = true;
			this.packagedZipFile.getParentFile().mkdirs();
			Files.createFile(this.packagedZipFile.toPath());
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Unable to create zip file for deployment " + this.name + " is there a conflicting file/folder in this location?", ex);
		}

		// Populate the package
		try (
			FileOutputStream zipFile = new FileOutputStream(this.packagedZipFile);
			ZipOutputStream zipStream = new ZipOutputStream(zipFile);

		) {
			final MessageDigest hash = MessageDigest.getInstance("MD5");

			// Add config file to zip
			this.addToZip(zipStream, this.configFile, hash);

			// Add the world files
			Files.walk(this.worldFolder.toPath())
				.forEach(path -> { this.addToZip(zipStream, path.toFile(), hash); });

			zipStream.close();
			zipFile.close();
			this.isVolitile = false;
			this.packageHash = MD5DigestToHexString(hash);
			return this.packageHash;
		} catch (IOException ex) {
			ex.printStackTrace();
			EscapeRoom._this().logSevere("An error occured while adding a file to the deployment package " + this.packagedZipFile.getName());
			throw new RuntimeException("Unable to create zip file for deployment " + this.name + " is there a conflicting file/folder in this location?", ex);
		} catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			EscapeRoom._this().logSevere("Your system does not suppor the MD5 hashing algorithm. Please report this to the developers.");
			EscapeRoom._this().logSevere("Canceling requested deployment");
			return null;
		}
	}

		private void addToZip(ZipOutputStream zipStream, File targetFile, MessageDigest hash) {
			try {
				if (targetFile.isDirectory()) { 
					EscapeRoom._this().logDebug("File " + targetFile + " is a directory");
				} else {
					EscapeRoom._this().logDebug("File " + targetFile + " is a file");
					ZipEntry ze = new ZipEntry(this.packagedZipFile.toPath().relativize(targetFile.toPath()).toString());
					zipStream.putNextEntry(ze);
					
					FileInputStream fis = new FileInputStream(targetFile);
					int readIn;
					byte[] in = new byte[8000];
					while((readIn = fis.read(in, 0, 8000)) != -1) {
						zipStream.write(in, 0, readIn);
						hash.update(in, 0, 8000);
					}
					fis.close();
					zipStream.closeEntry();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				EscapeRoom._this().logSevere("An error occured while adding a file to the deployment package " + this.packagedZipFile.getName());
				throw new RuntimeException("An error occured during deployment. Deployment package is corrupt. Please try agian.");
			}
		}

	public void unpack() {
		
	}

	/**
	 * Converts an MD5 MessageDigest to a human-readable hex string
	 * Credit https://stackoverflow.com/a/11665457
	 */
	public static String MD5DigestToHexString(MessageDigest digest) {
		byte[] byteData = digest.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++)
		sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	
		return sb.toString();
	}
}

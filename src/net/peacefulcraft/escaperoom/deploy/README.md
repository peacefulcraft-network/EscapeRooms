# net.peacefulcraft.escaperoom.deploy
This is a crude package manager for EscapeRooms. It is capable of:
- zipping EscapeRoomConfigurations and their coresponding world files.
- Using HTTPS with Basic Auth to upload and download the zip files.
- extract zip files and place ("install") the enclosed files where they need to go to run the eclosed map.
- Uses a manifest file with hashes to track available deployments in the "deployment network" (repository).
- Uses hashes to verify integirty of downloaded files.
- (Eventually) Will uses hashes to determine whether a map needs to be redownloaded for updates or if the current version is good.
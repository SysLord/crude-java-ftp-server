package de.syslord.crudeftpserver.virtualfilesystem;

import java.time.LocalDateTime;

import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;

import de.syslord.crudeftpserver.utils.SimpleFile;

public class VirtualFileSystemView implements FileSystemView {

	private VirtualFtpRootDir home;

	private User user;

	private FileUploadListener fileUploadListener;

	public VirtualFileSystemView(User user) {
		this.user = user;
		this.home = new VirtualFtpRootDir(this);
	}

	public void setFileUplodedListener(FileUploadListener fileUploadedListener) {
		this.fileUploadListener = fileUploadedListener;
	}

	public void onFileUploadDone(VirtualFtpFile file) {
		SimpleFile simpleFile = new SimpleFile(file.getName(), file.getData(), user.getName(), LocalDateTime.now());
		fileUploadListener.onFileUploadDone(simpleFile);
	}

	@Override
	public FtpFile getHomeDirectory() throws FtpException {
		return home;
	}

	@Override
	public FtpFile getWorkingDirectory() throws FtpException {
		return home;
	}

	@Override
	public boolean changeWorkingDirectory(String dir) throws FtpException {
		// Clients seem to expect this to be working for . .. and /
		if (".".equals(dir) || "/".equals(dir) || "./".equals(dir) || "..".equals(dir)) {
			return true;
		}
		return false;
	}

	@Override
	public FtpFile getFile(String file) throws FtpException {
		return home.getFile(file);
	}

	@Override
	public boolean isRandomAccessible() throws FtpException {
		return false;
	}

	@Override
	public void dispose() {
		//
	}

}

package de.syslord.crudeftpserver.virtualfilesystem;

import java.util.HashMap;
import java.util.Map;

import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;

public class VirtualFtpFilesystemFactory implements FileSystemFactory {

	private Map<User, VirtualFileSystemView> fileSystems = new HashMap<>();

	private FileUploadListener fileUploadedListener;

	@Override
	public FileSystemView createFileSystemView(User user) throws FtpException {
		VirtualFileSystemView virtualFileSystemView = new VirtualFileSystemView(user);
		virtualFileSystemView.setFileUplodedListener(fileUploadedListener);
		fileSystems.put(user, virtualFileSystemView);
		return virtualFileSystemView;
	}

	public void setFileUploadedListener(FileUploadListener fileUploadedListener) {
		this.fileUploadedListener = fileUploadedListener;
	}

}

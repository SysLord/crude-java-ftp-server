package de.syslord.crudeftpserver;

import java.util.List;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;

import de.syslord.crudeftpserver.users.DynamicUserManager;
import de.syslord.crudeftpserver.users.FtpServerUser;
import de.syslord.crudeftpserver.virtualfilesystem.FileUploadListener;
import de.syslord.crudeftpserver.virtualfilesystem.VirtualFtpFilesystemFactory;

public class CrudeFtpServer {

	private int port;

	private FileUploadListener fileUploadedListener;

	private List<FtpServerUser> users;

	public CrudeFtpServer(int port, FileUploadListener fileUploadedListener, List<FtpServerUser> users) {
		this.port = port;
		this.fileUploadedListener = fileUploadedListener;
		this.users = users;
	}

	public void start() throws FtpException {
		FtpServerFactory serverFactory = new FtpServerFactory();

		ListenerFactory factory = new ListenerFactory();
		factory.setPort(port);
		serverFactory.addListener("default", factory.createListener());

		serverFactory.setUserManager(new DynamicUserManager(users));

		VirtualFtpFilesystemFactory fileSystem = new VirtualFtpFilesystemFactory();
		fileSystem.setFileUploadedListener(fileUploadedListener);
		serverFactory.setFileSystem(fileSystem);

		FtpServer server = serverFactory.createServer();
		server.start();
	}

}

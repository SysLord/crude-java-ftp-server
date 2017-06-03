package de.syslord.crudeftpserver;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Ignore;
import org.junit.Test;

import de.syslord.crudeftpserver.users.FtpServerUser;
import de.syslord.crudeftpserver.utils.PolledQueue;
import de.syslord.crudeftpserver.utils.SimpleFile;

public class CrudeFtpServerTest {

	private static final int PORT = 2222;

	private static final int QUEUE_MAX_ITEMS = 10;

	private static final FtpServerUser FTP_SERVER_USER = new FtpServerUser("name", "password", true, 2);

	@Ignore
	@Test
	public void testStartCrudeFtpServer() throws Exception {
		PolledQueue<SimpleFile> polledQueue = new PolledQueue<SimpleFile>(fileUploadedPollingListener(), QUEUE_MAX_ITEMS);
		polledQueue.start();

		CrudeFtpServer crudeFtpServer = new CrudeFtpServer(PORT, file -> polledQueue.add(file), Arrays.asList(FTP_SERVER_USER));
		crudeFtpServer.start();

		// needs sleep
	}

	private Consumer<SimpleFile> fileUploadedPollingListener() {
		return file -> System.out.println("new file: " + file);
	}
}

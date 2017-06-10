package de.syslord.crudeftpserver;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.Ignore;
import org.junit.Test;

import de.syslord.crudeftpserver.users.FtpServerUser;
import de.syslord.crudeftpserver.utils.PolledQueue;
import de.syslord.crudeftpserver.utils.SimpleFile;

public class CrudeFtpServerTest {

	private static final int POLL_DELAY_SECONDS = 10;

	private static final int PORT = 2222;

	private static final int QUEUE_MAX_ITEMS = 10;

	private static final FtpServerUser FTP_SERVER_USER = new FtpServerUser("name", "password", true, 2);

	@Ignore
	@Test
	public void testStartCrudeFtpServer_Manually() throws Exception {
		PolledQueue<SimpleFile> polledQueue = new PolledQueue<SimpleFile>(
				fileUploadedPollingListener(),
				QUEUE_MAX_ITEMS, POLL_DELAY_SECONDS);
		polledQueue.start();

		CrudeFtpServer crudeFtpServer = new CrudeFtpServer(PORT, file -> polledQueue.add(file), Arrays.asList(FTP_SERVER_USER));
		crudeFtpServer.setPassivePortsString("50000-50100");
		crudeFtpServer.start();

		// you can test the server for 5 minutes
		Thread.sleep(5 * 60 * 1000);

		crudeFtpServer.stop();
		polledQueue.stop();
	}

	private Consumer<SimpleFile> fileUploadedPollingListener() {
		return file -> System.out.println("new file: " + file);
	}
}

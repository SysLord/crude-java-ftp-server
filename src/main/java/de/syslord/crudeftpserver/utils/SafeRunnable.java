package de.syslord.crudeftpserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutorServices just stop on first Exception. This is used to wrap the Runnable.
 */
public class SafeRunnable {

	private static final Logger logger = LoggerFactory.getLogger(SafeRunnable.class);

	public static Runnable of(Runnable unsafe) {
		return () -> {
			try {
				unsafe.run();
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		};
	}

}

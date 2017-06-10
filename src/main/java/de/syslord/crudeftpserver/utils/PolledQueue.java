package de.syslord.crudeftpserver.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PolledQueue<T> {

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private Consumer<T> listener;

	private List<T> queue = Collections.synchronizedList(new LinkedList<T>());

	private int maxCapacity;

	private boolean started = false;

	private long pollInterval;

	/**
	 *
	 * @param listener gets called regularly for all items in the queue
	 * @param maxCapacity when more than maxCapacity items are added during the poll interval the old ones are
	 *            silently dropped
	 */
	public PolledQueue(Consumer<T> listener, int maxCapacity, long pollInterval) {
		this.listener = listener;
		this.maxCapacity = maxCapacity;
		this.pollInterval = pollInterval;
	}

	public void start() {
		if (started) {
			throw new IllegalStateException("polling already started");
		}

		started = true;
		scheduler.scheduleWithFixedDelay(SafeRunnable.of(() -> poll()), 0, pollInterval, TimeUnit.SECONDS);
	}

	public void stop() {
		scheduler.shutdownNow();
	}

	private void poll() {
		T item;

		while ((item = getQueueItem()) != null) {
			listener.accept(item);
		}
	}

	public void add(T item) {
		addQueueItem(item);
	}

	private synchronized T getQueueItem() {
		return queue.isEmpty() ? null : queue.remove(0);
	}

	private synchronized void addQueueItem(T item) {
		if (queue.size() >= maxCapacity) {
			queue.remove(0);
		}
		queue.add(item);
	}

}

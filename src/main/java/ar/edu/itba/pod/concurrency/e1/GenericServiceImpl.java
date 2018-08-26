package ar.edu.itba.pod.concurrency.e1;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

/**
 * Basic implementation of {@link GenericService}.
 */
public class GenericServiceImpl implements GenericService {

	private int visitCounter;
	private Queue<String> serviceQueue;

	public GenericServiceImpl() {
		this.visitCounter = 0;
		this.serviceQueue = new LinkedList<>();
	}

	@Override
	public String echo(String message) {
		return message;
	}

	@Override
	public String toUpper(String message) {
		if (message == null) return null;
		return message.toUpperCase();
	}

	@Override
	public synchronized void addVisit() {

		this.visitCounter++;
	}

	@Override
	public synchronized int getVisitCount() {
		return this.visitCounter;
	}

	@Override
	public boolean isServiceQueueEmpty() {
		return serviceQueue.isEmpty();
	}

	@Override
	public void addToServiceQueue(String name) {
		if (name == null) throw new NullPointerException("Name is null");
		this.serviceQueue.add(name);
	}

	@Override
	public String getFirstInServiceQueue() {
		if (serviceQueue.isEmpty()) throw new IllegalStateException("Queue is empty");
		return serviceQueue.poll();
	}
}
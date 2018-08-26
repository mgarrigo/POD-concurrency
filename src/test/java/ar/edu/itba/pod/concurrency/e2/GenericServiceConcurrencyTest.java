package ar.edu.itba.pod.concurrency.e2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.pod.concurrency.e1.GenericService;
import ar.edu.itba.pod.concurrency.e1.GenericServiceImpl;

/**
 * Unit test for {@link GenericService} using {@link Thread}s
 */
public class GenericServiceConcurrencyTest {
    private static final int VISITS_PER_VISITOR = 500;
    private static final int EXPECTED_VISITS = 2500;

    private GenericService service;

    @Before
    public final void before() {
        service = new GenericServiceImpl();
    }

    /** Realiza 100 visitas al servicio */
    private final Runnable visitor = () -> {
        for (int i = 0; i < VISITS_PER_VISITOR; i++) {
            service.addVisit();
        }
        return;
    };

    // instanciar el pool.
    private final ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * generates 5 threads with {@link #visitor} and runs them.
     */
    @Test
    public final void visit_count_with_thread_start() throws InterruptedException {
        Queue<Thread> threads = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(visitor);
            thread.start();
            threads.add(thread);
        }
        while(!threads.isEmpty()) {
            threads.poll().join(1000);
        }
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates 5 threads with {@link #visitor} and runs them submiting it via
     * the {@link ExecutorService}
     */
    @Test
    public final void visit_count_with_executor_submit() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            pool.submit(visitor);

        }

        pool.shutdown();
        pool.awaitTermination(4, TimeUnit.SECONDS);
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates 5 threads with {@link #visitor} and runs with
     * {@link ExecutorService#invokeAll(Collection)}
     */
    @Test
    public final void visit_count_with_executor_invoke() throws InterruptedException {
        List<Callable<Object>> callables = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            callables.add(Executors.callable(visitor));
        }
        pool.invokeAll(callables);
        pool.shutdown();
        pool.awaitTermination(4, TimeUnit.SECONDS);
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates 5 Threads of a Runnable that sleeps for 10 seconds an inserts
     * 10 elements into the service queue. Once all threads are done, the test
     * should check to see of the queue is empty
     */
    @Test
    public final void concurrent_queue() {
        assertTrue(service.isServiceQueueEmpty());
    }
}

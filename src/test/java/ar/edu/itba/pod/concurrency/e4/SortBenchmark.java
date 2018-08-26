package ar.edu.itba.pod.concurrency.e4;

import java.util.*;
import java.util.function.Consumer;

import org.junit.Test;

/**
 * Benchmark to compare between {@link Arrays#parallelSort(int[])} and
 * {@link Arrays#sort(int[])}
 */
public class SortBenchmark {

    private int[] createRandomIntArray(int n) {
        Random rand = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = rand.nextInt();
        }
        return array;
    }

    @Test
    public void benchmark_all() {
        List<int[]> arrays = new LinkedList<>();
        arrays.add(createRandomIntArray(1000000));
        arrays.add(createRandomIntArray(25000000));
        arrays.add(createRandomIntArray(5000000));

        System.out.println("Parallel Sort");

        int iterations = 4;

        int arrayIndex = 1;
        for (int[] a: arrays) {
            System.out.println("Arrays.sort()");

            long timeAccum = 0L;

            for (int i = 0; i < iterations; i++) {
                long startTime = System.currentTimeMillis();

                Arrays.sort(a);

                long timeTaken = System.currentTimeMillis() - startTime;
                timeAccum += timeTaken;
            }

            double avg = timeAccum / iterations;
            System.out.println("Array: " + arrayIndex++ + " AvgTime: " + avg);

        }


        arrayIndex = 1;

        for (int[] a: arrays) {
            System.out.println("Arrays.parallelSort()");

            long timeAccum = 0L;

            for (int i = 0; i < iterations; i++) {
                long startTime = System.currentTimeMillis();

                Arrays.parallelSort(a);

                long timeTaken = System.currentTimeMillis() - startTime;
                timeAccum += timeTaken;
            }

            double avg = timeAccum / iterations;
            System.out.println("Array: " + arrayIndex++ + " AvgTime: " + avg);
        }

    }

}

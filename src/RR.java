// RR.java
// Round Robin (RR) - preemptive CPU scheduling.
// Each process runs for at most one time quantum, then goes to the back of the ready queue.
// Written by: John Michael Tupas

public class RR implements CpuScheduler {

    public CpuResult schedule(int[] arrival, int[] burst, int quantum) {
        int n = arrival.length;

        // order = the process numbers sorted by arrival time (bubble sort)
        // bubble sort only swaps when strictly greater, so ties stay in PID order
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arrival[order[j]] > arrival[order[j + 1]]) {
                    int temp = order[j];
                    order[j] = order[j + 1];
                    order[j + 1] = temp;
                }
            }
        }

        // copy the burst times because we will subtract from them
        int totalBurst = 0;
        int[] remaining = new int[n];
        for (int i = 0; i < n; i++) {
            remaining[i] = burst[i];
            totalBurst = totalBurst + burst[i];
        }

        CpuResult result = new CpuResult(n, totalBurst + n);
        boolean[] started = new boolean[n];

        // the ready queue is a simple array: take from front, add at rear
        int[] queue = new int[totalBurst + n];
        int front = 0;
        int rear = 0;

        int next = 0; // position in order[] of the next process that has not arrived yet
        int time = 0;
        int finished = 0;

        while (finished < n) {

            // add every process that has arrived by now to the queue
            while (next < n && arrival[order[next]] <= time) {
                queue[rear] = order[next];
                rear++;
                next++;
            }

            if (front == rear) {
                // queue is empty, so the CPU is idle until the next arrival
                int nextArrival = arrival[order[next]];
                result.addBlock("Idle", time, nextArrival);
                time = nextArrival;
            } else {
                // take the first process in the queue
                int p = queue[front];
                front++;

                // response time is only recorded the first time it gets the CPU
                if (!started[p]) {
                    result.response[p] = time - arrival[p];
                    started[p] = true;
                }

                // run for one quantum, or less if it finishes sooner
                int run = quantum;
                if (remaining[p] < quantum) {
                    run = remaining[p];
                }
                result.addBlock("P" + (p + 1), time, time + run);
                time = time + run;
                remaining[p] = remaining[p] - run;

                // new arrivals enter the queue first, before the process that just ran
                while (next < n && arrival[order[next]] <= time) {
                    queue[rear] = order[next];
                    rear++;
                    next++;
                }

                if (remaining[p] > 0) {
                    // not done yet, go to the back of the queue
                    queue[rear] = p;
                    rear++;
                } else {
                    result.completion[p] = time;
                    finished++;
                }
            }
        }

        result.computeTimes(arrival, burst);
        return result;
    }
}

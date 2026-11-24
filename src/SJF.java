// SJF.java
// Shortest Job First (SJF) - non-preemptive CPU scheduling.
// At every decision, pick the arrived process with the smallest burst time and run it to the end.
// Written by: Aliseo Villanueva

public class SJF implements CpuScheduler {

    public CpuResult schedule(int[] arrival, int[] burst, int quantum) {
        int n = arrival.length;
        CpuResult result = new CpuResult(n, n * 2); // at most n process blocks + n idle blocks
        boolean[] done = new boolean[n];
        int time = 0;
        int finished = 0;

        while (finished < n) {

            // find the process that already arrived and has the smallest burst time
            // tie: earlier arrival wins. still tied: lower PID wins because we check P1 first
            int pick = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= time) {
                    if (pick == -1
                            || burst[i] < burst[pick]
                            || (burst[i] == burst[pick] && arrival[i] < arrival[pick])) {
                        pick = i;
                    }
                }
            }

            if (pick == -1) {
                // nobody has arrived yet, so the CPU is idle until the next arrival
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (!done[i] && arrival[i] < nextArrival) {
                        nextArrival = arrival[i];
                    }
                }
                result.addBlock("Idle", time, nextArrival);
                time = nextArrival;
            } else {
                // run the picked process from start to finish (non-preemptive)
                result.response[pick] = time - arrival[pick];
                result.addBlock("P" + (pick + 1), time, time + burst[pick]);
                time = time + burst[pick];
                result.completion[pick] = time;
                done[pick] = true;
                finished++;
            }
        }

        result.computeTimes(arrival, burst);
        return result;
    }
}

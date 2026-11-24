// CpuResult.java
// Holds the answer of a CPU scheduling algorithm: the times per process and the gantt chart.
// Written by: Ivan Allen Torres (Leader)

public class CpuResult {

    // one value per process, index 0 is P1, index 1 is P2, and so on
    public int[] completion;
    public int[] turnaround;
    public int[] waiting;
    public int[] response;

    // the gantt chart blocks: name ("P1" or "Idle"), start time and end time
    public String[] ganttName;
    public int[] ganttStart;
    public int[] ganttEnd;
    public int ganttCount = 0;

    public CpuResult(int n, int maxBlocks) {
        completion = new int[n];
        turnaround = new int[n];
        waiting = new int[n];
        response = new int[n];
        ganttName = new String[maxBlocks];
        ganttStart = new int[maxBlocks];
        ganttEnd = new int[maxBlocks];
    }

    // adds a block to the gantt chart
    // if the same process runs again right away, we just make the last block longer
    public void addBlock(String name, int start, int end) {
        if (ganttCount > 0 && ganttName[ganttCount - 1].equals(name) && ganttEnd[ganttCount - 1] == start) {
            ganttEnd[ganttCount - 1] = end;
        } else {
            ganttName[ganttCount] = name;
            ganttStart[ganttCount] = start;
            ganttEnd[ganttCount] = end;
            ganttCount++;
        }
    }

    // Turnaround = Completion - Arrival, Waiting = Turnaround - Burst
    public void computeTimes(int[] arrival, int[] burst) {
        for (int i = 0; i < arrival.length; i++) {
            turnaround[i] = completion[i] - arrival[i];
            waiting[i] = turnaround[i] - burst[i];
        }
    }

    // time when the last process finished (time starts at 0)
    public int totalTime() {
        return ganttEnd[ganttCount - 1];
    }

    // time the CPU was running a process (all blocks except Idle)
    public int busyTime() {
        int busy = 0;
        for (int i = 0; i < ganttCount; i++) {
            if (!ganttName[i].equals("Idle")) {
                busy = busy + (ganttEnd[i] - ganttStart[i]);
            }
        }
        return busy;
    }

    // Utilization = busy time / total time x 100
    public double utilization() {
        return busyTime() * 100.0 / totalTime();
    }

    // Throughput = number of processes / total time
    public double throughput() {
        return (double) completion.length / totalTime();
    }

    // average of an array of numbers
    public static double average(int[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum = sum + values[i];
        }
        return (double) sum / values.length;
    }
}

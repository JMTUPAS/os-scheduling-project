// SampleData.java
// Sample problems used by the "Load Sample Data" buttons and by TestRunner.
// CPU samples: our own 10-process problems, solved by hand (the professor wants 10 to 20 processes).
// Disk sample: the classic example from Silberschatz, Galvin, Gagne - Operating System Concepts.
// Written by: John Michael Tupas (Testing Lead)

public class SampleData {

    // SJF example: P1 to P10 (has a tie, P2 and P4, and the CPU is idle from 32 to 40)
    public static int[] SJF_ARRIVAL = {0, 1, 2, 3, 4, 5, 6, 7, 40, 41};
    public static int[] SJF_BURST = {6, 3, 8, 3, 2, 4, 1, 5, 3, 2};

    // Round Robin example: P1 to P10, quantum 3 (P4 and P10 arrive exactly when a turn ends)
    public static int[] RR_ARRIVAL = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static int[] RR_BURST = {5, 3, 8, 2, 6, 4, 3, 1, 5, 2};
    public static int RR_QUANTUM = 3;

    // disk example: head at 53, disk has cylinders 0 to 199
    public static int[] DISK_REQUESTS = {98, 183, 37, 122, 14, 124, 65, 67};
    public static int DISK_HEAD = 53;
    public static int DISK_SIZE = 200;
}

// SampleData.java
// Sample problems used by the "Load Sample Data" buttons and by TestRunner.
// Taken from the classic examples in Silberschatz, Galvin, Gagne - Operating System Concepts.
// Written by: John Michael Tupas (Testing Lead)

public class SampleData {

    // SJF example: P1 to P4
    public static int[] SJF_ARRIVAL = {0, 2, 4, 5};
    public static int[] SJF_BURST = {7, 4, 1, 4};

    // Round Robin example: P1 to P3, quantum 4
    public static int[] RR_ARRIVAL = {0, 0, 0};
    public static int[] RR_BURST = {24, 3, 3};
    public static int RR_QUANTUM = 4;

    // disk example: head at 53, disk has cylinders 0 to 199
    public static int[] DISK_REQUESTS = {98, 183, 37, 122, 14, 124, 65, 67};
    public static int DISK_HEAD = 53;
    public static int DISK_SIZE = 200;
}

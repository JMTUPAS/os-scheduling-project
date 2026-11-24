// CpuScheduler.java
// Shared interface for the CPU scheduling algorithms.
// Every CPU algorithm (SJF, RR) has this same method so the screen can use any of them.
// Written by: Ivan Allen Torres (Leader)

public interface CpuScheduler {

    // quantum is only used by Round Robin, the other algorithms ignore it
    CpuResult schedule(int[] arrival, int[] burst, int quantum);
}

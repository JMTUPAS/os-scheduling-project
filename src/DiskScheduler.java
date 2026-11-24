// DiskScheduler.java
// Shared interface for the disk scheduling algorithms.
// Every disk algorithm (SCAN, C-LOOK) has this same method so the screen can use any of them.
// Written by: Ivan Allen Torres (Leader)

public interface DiskScheduler {

    // goRight = true means the head moves toward the last cylinder first
    // goRight = false means the head moves toward cylinder 0 first
    DiskResult schedule(int[] requests, int head, int diskSize, boolean goRight);
}

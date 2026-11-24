// DiskResult.java
// Holds the answer of a disk scheduling algorithm: the path of the disk head.
// Written by: Ivan Allen Torres (Leader)

public class DiskResult {

    // every position the head visits, in order. path[0] is the starting head position
    public int[] path;
    public int count = 0;

    public DiskResult(int maxPoints) {
        path = new int[maxPoints];
    }

    // adds the next position of the head
    public void add(int cylinder) {
        path[count] = cylinder;
        count++;
    }

    // how far the head moved in one step (from point step-1 to point step)
    public int movement(int step) {
        return Math.abs(path[step] - path[step - 1]);
    }

    // total head movement = sum of all the steps
    public int totalMovement() {
        int total = 0;
        for (int i = 1; i < count; i++) {
            total = total + movement(i);
        }
        return total;
    }

    // average seek distance = total head movement / number of requests
    public double averageSeek(int numberOfRequests) {
        return (double) totalMovement() / numberOfRequests;
    }
}

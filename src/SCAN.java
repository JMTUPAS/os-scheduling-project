// SCAN.java
// SCAN (elevator) disk scheduling.
// The head moves in one direction serving requests until the physical end of the disk,
// then turns around and serves the remaining requests going the other way.
// Written by: Nicholo Castor

import java.util.Arrays;

public class SCAN implements DiskScheduler {

    public DiskResult schedule(int[] requests, int head, int diskSize, boolean goRight) {
        int n = requests.length;

        // sort a copy of the requests from lowest to highest cylinder
        int[] sorted = requests.clone();
        Arrays.sort(sorted);

        DiskResult result = new DiskResult(n + 2); // head + requests + disk end
        result.add(head);

        if (goRight) {
            // going up: serve every request at or above the head
            for (int i = 0; i < n; i++) {
                if (sorted[i] >= head) {
                    result.add(sorted[i]);
                }
            }
            // keep going to the last cylinder of the disk
            if (result.path[result.count - 1] != diskSize - 1) {
                result.add(diskSize - 1);
            }
            // turn around: serve the requests below the head, going down
            for (int i = n - 1; i >= 0; i--) {
                if (sorted[i] < head) {
                    result.add(sorted[i]);
                }
            }
        } else {
            // going down: serve every request at or below the head
            for (int i = n - 1; i >= 0; i--) {
                if (sorted[i] <= head) {
                    result.add(sorted[i]);
                }
            }
            // keep going to cylinder 0
            if (result.path[result.count - 1] != 0) {
                result.add(0);
            }
            // turn around: serve the requests above the head, going up
            for (int i = 0; i < n; i++) {
                if (sorted[i] > head) {
                    result.add(sorted[i]);
                }
            }
        }

        return result;
    }
}

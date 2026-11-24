// CLOOK.java
// C-LOOK (circular LOOK) disk scheduling.
// The head moves in one direction serving requests until the last request,
// then jumps to the farthest request on the other side and continues in the SAME direction.
// The jump is counted in the total head movement.
// Written by: Ivan Allen Torres

import java.util.Arrays;

public class CLOOK implements DiskScheduler {

    public DiskResult schedule(int[] requests, int head, int diskSize, boolean goRight) {
        int n = requests.length;

        // sort a copy of the requests from lowest to highest cylinder
        int[] sorted = requests.clone();
        Arrays.sort(sorted);

        DiskResult result = new DiskResult(n + 1); // head + requests
        result.add(head);

        if (goRight) {
            // going up: serve every request at or above the head
            for (int i = 0; i < n; i++) {
                if (sorted[i] >= head) {
                    result.add(sorted[i]);
                }
            }
            // jump to the lowest request, then keep going up
            for (int i = 0; i < n; i++) {
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
            // jump to the highest request, then keep going down
            for (int i = n - 1; i >= 0; i--) {
                if (sorted[i] > head) {
                    result.add(sorted[i]);
                }
            }
        }

        return result;
    }
}

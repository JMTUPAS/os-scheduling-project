// TestRunner.java
// Checks the 4 algorithms against answers we solved by hand. 
// Run: java -cp out TestRunner
// Tupas (Testing Lead)

public class TestRunner {

    static int failed = 0;

    // prints PASS or FAIL for one value
    static void check(String name, double actual, double expected) {
        if (Math.abs(actual - expected) < 0.01) {
            System.out.println("PASS  " + name);
        } else {
            System.out.println("FAIL  " + name + "  expected " + expected + " but got " + actual);
            failed++;
        }
    }

    public static void main(String[] args) {

        // SJF sample: order P1 0-7, P3 7-8, P2 8-12, P4 12-16
        CpuResult sjf = new SJF().schedule(SampleData.SJF_ARRIVAL, SampleData.SJF_BURST, 0);
        check("SJF completion P1", sjf.completion[0], 7);
        check("SJF completion P2", sjf.completion[1], 12);
        check("SJF completion P3", sjf.completion[2], 8);
        check("SJF completion P4", sjf.completion[3], 16);
        check("SJF average waiting", CpuResult.average(sjf.waiting), 4.0);

        // SJF with idle time: P1 runs 0-2, CPU idle 2-5, P2 runs 5-8
        CpuResult idle = new SJF().schedule(new int[] {0, 5}, new int[] {2, 3}, 0);
        check("SJF idle block start", idle.ganttStart[1], 2);
        check("SJF idle block end", idle.ganttEnd[1], 5);
        check("SJF utilization with idle", idle.utilization(), 62.5);

        // Round Robin sample: average waiting = (6 + 4 + 7) / 3
        CpuResult rr = new RR().schedule(SampleData.RR_ARRIVAL, SampleData.RR_BURST, SampleData.RR_QUANTUM);
        check("RR completion P1", rr.completion[0], 30);
        check("RR completion P2", rr.completion[1], 7);
        check("RR completion P3", rr.completion[2], 10);
        check("RR average waiting", CpuResult.average(rr.waiting), 17.0 / 3);
        check("RR response P3", rr.response[2], 7);

        // Round Robin rule: new arrival (P2 at time 2) goes in the queue before preempted P1
        CpuResult rr2 = new RR().schedule(new int[] {0, 2}, new int[] {4, 2}, 2);
        check("RR new arrival first, P2 completion", rr2.completion[1], 4);
        check("RR new arrival first, P1 completion", rr2.completion[0], 6);

        // SCAN toward 0: 53 37 14 0 65 67 98 122 124 183
        DiskResult scanLeft = new SCAN().schedule(SampleData.DISK_REQUESTS, 53, 200, false);
        check("SCAN left total movement", scanLeft.totalMovement(), 236);

        // SCAN toward 199: 53 65 67 98 122 124 183 199 37 14
        DiskResult scanRight = new SCAN().schedule(SampleData.DISK_REQUESTS, 53, 200, true);
        check("SCAN right total movement", scanRight.totalMovement(), 331);

        // C-LOOK toward 199: 53 65 67 98 122 124 183 14 37
        DiskResult clookRight = new CLOOK().schedule(SampleData.DISK_REQUESTS, 53, 200, true);
        check("C-LOOK right total movement", clookRight.totalMovement(), 322);
        check("C-LOOK right jump lands on 14", clookRight.path[7], 14);

        // C-LOOK toward 0: 53 37 14 183 124 122 98 67 65
        DiskResult clookLeft = new CLOOK().schedule(SampleData.DISK_REQUESTS, 53, 200, false);
        check("C-LOOK left total movement", clookLeft.totalMovement(), 326);

        // duplicate requests are served one after another (zero movement)
        DiskResult dup = new CLOOK().schedule(new int[] {60, 60}, 50, 200, true);
        check("duplicate requests movement", dup.totalMovement(), 10);

        System.out.println();
        if (failed == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println(failed + " TEST(S) FAILED");
        }
    }
}

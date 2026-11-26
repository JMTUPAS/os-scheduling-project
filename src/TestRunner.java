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

        // SJF textbook example: order P1 0-7, P3 7-8, P2 8-12, P4 12-16
        CpuResult sjf = new SJF().schedule(new int[] {0, 2, 4, 5}, new int[] {7, 4, 1, 4}, 0);
        check("SJF completion P1", sjf.completion[0], 7);
        check("SJF completion P2", sjf.completion[1], 12);
        check("SJF completion P3", sjf.completion[2], 8);
        check("SJF completion P4", sjf.completion[3], 16);
        check("SJF average waiting", CpuResult.average(sjf.waiting), 4.0);

        // SJF 10-process sample: P1 0-6, P7 6-7, P5 7-9, P2 9-12, P4 12-15, P6 15-19, P8 19-24, P3 24-32,
        // Idle 32-40, P9 40-43, P10 43-45
        CpuResult sjf10 = new SJF().schedule(SampleData.SJF_ARRIVAL, SampleData.SJF_BURST, 0);
        check("SJF sample completion P2 (tie with P4, arrived first)", sjf10.completion[1], 12);
        check("SJF sample completion P3", sjf10.completion[2], 32);
        check("SJF sample completion P10", sjf10.completion[9], 45);
        check("SJF sample average turnaround", CpuResult.average(sjf10.turnaround), 10.3);
        check("SJF sample average waiting", CpuResult.average(sjf10.waiting), 6.6);
        check("SJF sample utilization (idle 32-40)", sjf10.utilization(), 37 * 100.0 / 45);

        // SJF with idle time: P1 runs 0-2, CPU idle 2-5, P2 runs 5-8
        CpuResult idle = new SJF().schedule(new int[] {0, 5}, new int[] {2, 3}, 0);
        check("SJF idle block start", idle.ganttStart[1], 2);
        check("SJF idle block end", idle.ganttEnd[1], 5);
        check("SJF utilization with idle", idle.utilization(), 62.5);

        // Round Robin 10-process sample, quantum 3
        CpuResult rr10 = new RR().schedule(SampleData.RR_ARRIVAL, SampleData.RR_BURST, SampleData.RR_QUANTUM);
        check("RR sample completion P1", rr10.completion[0], 13);
        check("RR sample completion P3 (last)", rr10.completion[2], 39);
        check("RR sample completion P4 (arrived at 3, before P1)", rr10.completion[3], 11);
        check("RR sample average turnaround", CpuResult.average(rr10.turnaround), 20.3);
        check("RR sample average waiting", CpuResult.average(rr10.waiting), 16.4);
        check("RR sample average response", CpuResult.average(rr10.response), 9.2);

        // Round Robin textbook example: average waiting = (6 + 4 + 7) / 3
        CpuResult rr = new RR().schedule(new int[] {0, 0, 0}, new int[] {24, 3, 3}, 4);
        check("RR completion P1", rr.completion[0], 30);
        check("RR completion P2", rr.completion[1], 7);
        check("RR completion P3", rr.completion[2], 10);
        check("RR average waiting", CpuResult.average(rr.waiting), 17.0 / 3);
        check("RR response P3", rr.response[2], 7);

        // Round Robin rule: new arrival (P2 at time 2) goes in the queue before preempted P1
        CpuResult rr2 = new RR().schedule(new int[] {0, 2}, new int[] {4, 2}, 2);
        check("RR new arrival first, P2 completion", rr2.completion[1], 4);
        check("RR new arrival first, P1 completion", rr2.completion[0], 6);

        // SCAN toward 0: 50 35 25 10 0 60 75 90 120 150 170 190
        DiskResult scanLeft = new SCAN().schedule(SampleData.DISK_REQUESTS, 50, 200, false);
        check("SCAN left total movement", scanLeft.totalMovement(), 240);

        // SCAN toward 199: 50 60 75 90 120 150 170 190 199 35 25 10
        DiskResult scanRight = new SCAN().schedule(SampleData.DISK_REQUESTS, 50, 200, true);
        check("SCAN right total movement", scanRight.totalMovement(), 338);

        // C-LOOK toward 199: 50 60 75 90 120 150 170 190 10 25 35
        DiskResult clookRight = new CLOOK().schedule(SampleData.DISK_REQUESTS, 50, 200, true);
        check("C-LOOK right total movement", clookRight.totalMovement(), 345);
        check("C-LOOK right jump lands on 10", clookRight.path[8], 10);

        // C-LOOK toward 0: 50 35 25 10 190 170 150 120 90 75 60
        DiskResult clookLeft = new CLOOK().schedule(SampleData.DISK_REQUESTS, 50, 200, false);
        check("C-LOOK left total movement", clookLeft.totalMovement(), 350);

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

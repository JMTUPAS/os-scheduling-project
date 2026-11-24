# OS Scheduling Project - Group 6

ICS26012 Operating Systems project. A Java Swing program that solves 2 CPU scheduling and 2 disk scheduling algorithms and draws the Gantt chart / head movement graph.

## Members

| Member | Algorithm | Role |
|---|---|---|
| Ivan Allen Torres | C-LOOK | Leader |
| Aliseo Villanueva | Shortest Job First (SJF), non-preemptive | Documentation Lead |
| Nicholo Castor | SCAN | Video Lead |
| John Michael Tupas | Round Robin (RR), preemptive | Testing Lead |

## How to run

Double-click `OSProject.jar`, or open a terminal in this folder and type:

```
java -jar OSProject.jar
```

Needs Java 8 or newer. No internet or database needed.

## How to build the jar

Needs the JDK. Double-click `build.bat`, or run it in a terminal. It compiles everything in `src` into `out` and makes `OSProject.jar`.

To run the tests (no GUI):

```
java -cp out TestRunner
```

## Files

| File | What it does | Owner |
|---|---|---|
| `Main.java` | main window, main menu, About screen | Torres |
| `CpuScheduler.java`, `DiskScheduler.java` | shared interfaces | Torres |
| `CpuResult.java`, `DiskResult.java` | hold the answers + formulas | Torres |
| `SJF.java` | Shortest Job First logic | Villanueva |
| `RR.java` | Round Robin logic | Tupas |
| `SCAN.java` | SCAN logic | Castor |
| `CLOOK.java` | C-LOOK logic | Torres |
| `CpuPanel.java` | CPU screen (tables, buttons) | Tupas |
| `GanttPanel.java` | draws the Gantt chart | Villanueva |
| `DiskPanel.java` | disk screen (tables, buttons) | Castor |
| `HeadPathPanel.java` | draws the head movement graph | Torres |
| `InputCheck.java` | input validation messages | Tupas |
| `SampleData.java`, `TestRunner.java` | sample problems and tests | Tupas |

The algorithm classes do not use any Swing code, so they can be tested alone (see `TestRunner`).

## Conventions we followed

**CPU scheduling**
- Time starts at 0 in whole time units. If no process is ready, the CPU is idle and the idle part is shown (gray) in the Gantt chart. Context switch time is 0.
- Ties: earlier arrival goes first, then lower PID.
- Round Robin: if a new arrival and a preempted process enter the ready queue at the same time, the new arrival is queued first.
- Turnaround = Completion - Arrival
- Waiting = Turnaround - Burst
- Response = First CPU time - Arrival
- CPU Utilization = busy time / total time x 100
- Throughput = number of processes / total time

**Disk scheduling**
- Cylinders run from 0 to N-1 (default N = 200).
- Head movement = sum of the absolute differences between each head position, starting from the initial head.
- Duplicate requests are served one after another (zero movement).
- A request on the same cylinder as the head is served first (zero movement).
- SCAN goes all the way to the physical end of the disk (0 or N-1) before turning around.
- C-LOOK turns at the last pending request, then jumps to the farthest request on the other side. The jump is counted in the total head movement and shown in the graph.
- Average seek distance = total head movement / number of requests.

## Input limits

| Input | Allowed |
|---|---|
| Number of processes | 1 to 20 |
| Arrival time | 0 to 999 |
| Burst time | 1 to 999 |
| Time quantum | 1 to 100 |
| Number of disk requests | 1 to 50 |
| Disk size | 2 to 10000 |
| Initial head / requests | 0 to disk size - 1 |

Empty, negative, decimal or letter inputs show an error message instead of crashing.

## Sample data

The "Load Sample Data" buttons use the classic examples from Silberschatz, Galvin, Gagne, *Operating System Concepts*:
- SJF: P1-P4, arrival 0, 2, 4, 5 and burst 7, 4, 1, 4
- RR: P1-P3, arrival 0, burst 24, 3, 3, quantum 4
- Disk: queue 98, 183, 37, 122, 14, 124, 65, 67, head 53, disk size 200 (SCAN goes left, C-LOOK goes right)

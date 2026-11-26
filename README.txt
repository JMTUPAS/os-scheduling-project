OS SCHEDULING PROJECT - GILAS PILIPINAS
=======================================

WHAT IS THIS?
This is a program that shows how a computer decides
which job to do first, and how a disk reads its data.

WHO MADE IT?
  Ivan Allen Torres    - C-LOOK          - Leader
  Aliseo Villanueva    - SJF             - Documentation
  Nicholo Castor       - SCAN            - Video
  John Michael Tupas   - Round Robin     - Testing

HOW DO I OPEN IT?
  1. Make sure Java is on the computer.
  2. Double-click OSProject.jar
  That's it!

HOW DO I USE IT?
  1. Pick a button on the menu.
  2. Type your numbers in the table.
     (Or click "Load Sample Data" to fill it for you.)
  3. Click "Compute".
  4. Look at the answers, the chart and the graph.
  5. Click "Reset / Clear" to start again.

THE 4 WAYS (ALGORITHMS)
  SJF          - The shortest job goes first.
  Round Robin  - Everyone takes turns for a short time.
  SCAN         - The disk head goes one way to the end,
                 then turns around, like an elevator.
  C-LOOK       - The disk head goes one way, then jumps
                 back to the other side and goes the same way again.

OUR RULES
  - SJF and Round Robin need 10 to 20 processes.
    Any other number shows an error message.
  - Time starts at 0.
  - If two jobs are tied, the one that came first goes first.
    If still tied, the smaller number (P1 before P2) goes first.
  - In Round Robin, a new job gets in line before
    the job that just had its turn.
  - The disk goes from 0 to 199 unless you change the size.
  - When the CPU has nothing to do, the chart shows "Idle".

HOW DO I REBUILD IT? (only if you change the code)
  Double-click build.bat
  It makes a new OSProject.jar for you.

WHERE ARE THE SAMPLE PROBLEMS FROM?
  SJF and Round Robin: our own 10-process problems, solved by hand.
  Disk: the book "Operating System Concepts" by Silberschatz.

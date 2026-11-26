// GanttPanel.java
// Draws the gantt chart on a time scale. Idle time is drawn in gray.
// Written by: Aliseo Villanueva

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GanttPanel extends JPanel {

    private CpuResult result; // nothing is drawn while this is null

    // one color per process: P1 uses colors[0], P2 uses colors[1], ... then it repeats
    private Color[] colors = {
        new Color(135, 206, 250), new Color(255, 182, 120), new Color(144, 238, 144),
        new Color(255, 160, 180), new Color(200, 170, 255), new Color(255, 230, 120),
        new Color(120, 220, 210), new Color(230, 180, 150), new Color(190, 220, 120),
        new Color(170, 180, 230)
    };

    public GanttPanel() {
        setBackground(Color.WHITE);
    }

    public void setResult(CpuResult result) {
        this.result = result;
        repaint(); // tells Swing to call paintComponent again
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (result == null) {
            return;
        }

        int left = 20;
        int top = 50;
        int height = 50;

        // how many pixels one time unit takes, so the chart fits the panel width
        double unit = (getWidth() - 2 * left) / (double) result.totalTime();

        int endX = left + (int) (result.totalTime() * unit); // where the last box ends
        int lastLabelEnd = -100; // where the last time label we wrote ends, so labels do not pile up
        int aboveEnd1 = -100; // where the last name written above the chart ends, on row 1 and row 2
        int aboveEnd2 = -100;

        for (int i = 0; i < result.ganttCount; i++) {
            int x1 = left + (int) (result.ganttStart[i] * unit);
            int x2 = left + (int) (result.ganttEnd[i] * unit);

            // fill the box: gray for idle, the process's own color for a process
            if (result.ganttName[i].equals("Idle")) {
                g.setColor(Color.LIGHT_GRAY);
            } else {
                int number = Integer.parseInt(result.ganttName[i].substring(1)); // "P3" -> 3
                g.setColor(colors[(number - 1) % colors.length]);
            }
            g.fillRect(x1, top, x2 - x1, height);

            // border, name inside the box, start time under the box
            g.setColor(Color.BLACK);
            g.drawRect(x1, top, x2 - x1, height);

            // write the name inside the box if it fits; if the box is too small, write it above the box
            // (2 rows above, so names of tiny boxes next to each other do not overlap)
            String name = result.ganttName[i];
            int nameWidth = g.getFontMetrics().stringWidth(name);
            if (x2 - x1 > nameWidth + 6) {
                g.drawString(name, x1 + 4, top + 30);
            } else if (x1 > aboveEnd1 + 2) {
                g.drawString(name, x1, top - 5);
                aboveEnd1 = x1 + nameWidth;
            } else if (x1 > aboveEnd2 + 2) {
                g.drawString(name, x1, top - 18);
                aboveEnd2 = x1 + nameWidth;
            }

            // write the start time only if it does not touch the last label or the end label
            String time = "" + result.ganttStart[i];
            int timeWidth = g.getFontMetrics().stringWidth(time); // width of the text in pixels
            if (x1 - 3 > lastLabelEnd + 5 && x1 - 3 + timeWidth + 5 < endX - 3) {
                g.drawString(time, x1 - 3, top + height + 15);
                lastLabelEnd = x1 - 3 + timeWidth;
            }
        }

        // the end time under the last box (always shown)
        g.drawString("" + result.totalTime(), endX - 3, top + height + 15);
    }
}

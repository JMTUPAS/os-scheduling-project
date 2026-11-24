// HeadPathPanel.java
// Draws the graph of the disk head's path.
// Left to right = cylinder number, top to bottom = order of the steps.
// Written by: Ivan Allen Torres

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class HeadPathPanel extends JPanel {

    private DiskResult result; // nothing is drawn while this is null
    private int diskSize;

    public HeadPathPanel() {
        setBackground(Color.WHITE);
    }

    public void setResult(DiskResult result, int diskSize) {
        this.result = result;
        this.diskSize = diskSize;
        repaint(); // tells Swing to call paintComponent again
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (result == null) {
            return;
        }

        int left = 30;
        int right = getWidth() - 40;
        int top = 50;
        int bottom = getHeight() - 20;

        // cylinder scale at the top: 0 on the left, last cylinder on the right
        g.setColor(Color.BLACK);
        g.drawLine(left, top - 15, right, top - 15);
        g.drawString("0", left - 3, top - 20);
        g.drawString("" + (diskSize - 1), right - 10, top - 20);

        // space between steps, so every step fits the panel height
        int rowGap = (bottom - top) / Math.max(1, result.count - 1);

        int prevX = 0;
        int prevY = 0;
        for (int i = 0; i < result.count; i++) {
            // change the cylinder number into an x position on the screen
            int x = left + result.path[i] * (right - left) / (diskSize - 1);
            int y = top + i * rowGap;

            // line from the previous point to this point
            if (i > 0) {
                g.setColor(Color.BLUE);
                g.drawLine(prevX, prevY, x, y);
            }

            // dot and cylinder number
            g.setColor(Color.RED);
            g.fillOval(x - 4, y - 4, 8, 8);
            g.setColor(Color.BLACK);
            g.drawString("" + result.path[i], x + 6, y + 4);

            prevX = x;
            prevY = y;
        }
    }
}

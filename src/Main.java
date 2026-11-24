// Main.java
// Starts the program. One window that swaps screens (CardLayout):
// main menu, the 4 algorithm screens, and the About screen.
// Written by: Ivan Allen Torres (Leader)

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Main extends JFrame implements ActionListener {

    // static so every screen's Back button can call Main.showScreen("menu")
    private static CardLayout cards = new CardLayout();
    private static JPanel screens = new JPanel(cards);

    private JButton sjfButton = new JButton("Shortest Job First (SJF)");
    private JButton rrButton = new JButton("Round Robin (RR)");
    private JButton scanButton = new JButton("SCAN");
    private JButton clookButton = new JButton("C-LOOK");
    private JButton aboutButton = new JButton("About");
    private JButton exitButton = new JButton("Exit");
    private JButton aboutBackButton = new JButton("Back");

    public Main() {
        setTitle("OS Scheduling Project - Group 6");
        setSize(1150, 720);
        setLocationRelativeTo(null); // center on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // every screen gets a name so we can switch to it
        screens.add(makeMenu(), "menu");
        screens.add(new CpuPanel("Shortest Job First (SJF) - Non-preemptive", new SJF(), false,
                SampleData.SJF_ARRIVAL, SampleData.SJF_BURST, 0), "sjf");
        screens.add(new CpuPanel("Round Robin (RR) - Preemptive", new RR(), true,
                SampleData.RR_ARRIVAL, SampleData.RR_BURST, SampleData.RR_QUANTUM), "rr");
        screens.add(new DiskPanel("SCAN Disk Scheduling", new SCAN(), false), "scan");
        screens.add(new DiskPanel("C-LOOK Disk Scheduling", new CLOOK(), true), "clook");
        screens.add(makeAbout(), "about");
        add(screens);

        showScreen("menu");
    }

    public static void showScreen(String name) {
        cards.show(screens, name);
    }

    private JPanel makeMenu() {
        JLabel title = new JLabel("Main Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        JLabel cpuLabel = new JLabel("CPU Scheduling", SwingConstants.CENTER);
        cpuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel diskLabel = new JLabel("Disk Scheduling", SwingConstants.CENTER);
        diskLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // one column of labels and buttons
        JPanel list = new JPanel(new GridLayout(0, 1, 8, 8));
        list.add(title);
        list.add(cpuLabel);
        list.add(sjfButton);
        list.add(rrButton);
        list.add(diskLabel);
        list.add(scanButton);
        list.add(clookButton);
        list.add(new JLabel(""));
        list.add(aboutButton);
        list.add(exitButton);

        sjfButton.addActionListener(this);
        rrButton.addActionListener(this);
        scanButton.addActionListener(this);
        clookButton.addActionListener(this);
        aboutButton.addActionListener(this);
        exitButton.addActionListener(this);

        // GridBagLayout puts the list in the middle of the window
        JPanel menu = new JPanel(new GridBagLayout());
        menu.add(list);
        return menu;
    }

    private JPanel makeAbout() {
        JLabel title = new JLabel("About - Group 6", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        // member list as a read-only table
        String[] columns = {"Member", "Algorithm", "Role"};
        String[][] rows = {
            {"Ivan Allen Torres", "C-LOOK", "Leader"},
            {"Aliseo Villanueva", "Shortest Job First (SJF)", "Documentation Lead"},
            {"Nicholo Castor", "SCAN", "Video Lead"},
            {"John Michael Tupas", "Round Robin (RR)", "Testing Lead"}
        };
        JTable table = new JTable(rows, columns);
        table.setDefaultEditor(Object.class, null);
        table.setRowHeight(28);

        JPanel backRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backRow.add(aboutBackButton);
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(backRow);
        top.add(title);
        aboutBackButton.addActionListener(this);

        JPanel about = new JPanel(new BorderLayout(10, 10));
        about.setBorder(BorderFactory.createEmptyBorder(20, 150, 300, 150));
        about.add(top, BorderLayout.NORTH);
        about.add(new JScrollPane(table), BorderLayout.CENTER);
        return about;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sjfButton) {
            showScreen("sjf");
        } else if (e.getSource() == rrButton) {
            showScreen("rr");
        } else if (e.getSource() == scanButton) {
            showScreen("scan");
        } else if (e.getSource() == clookButton) {
            showScreen("clook");
        } else if (e.getSource() == aboutButton) {
            showScreen("about");
        } else if (e.getSource() == aboutBackButton) {
            showScreen("menu");
        } else if (e.getSource() == exitButton) {
            int answer = JOptionPane.showConfirmDialog(this, "Exit the program?", "Exit", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        // use the Windows look so the buttons look normal
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // if it fails, Java just uses its default look
        }
        new Main().setVisible(true);
    }
}

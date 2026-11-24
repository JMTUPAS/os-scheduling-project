// CpuPanel.java
// The screen for the CPU scheduling algorithms (used by both SJF and Round Robin).
// Input table + buttons on top, result table, gantt chart and averages below.
// Written by: John Michael Tupas

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CpuPanel extends JPanel implements ActionListener, ChangeListener {

    // the algorithm this screen uses (SJF or RR) and its sample data
    private CpuScheduler scheduler;
    private boolean usesQuantum;
    private int[] sampleArrival;
    private int[] sampleBurst;
    private int sampleQuantum;

    // inputs
    private JSpinner countSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
    private JTextField quantumField = new JTextField(4);
    private JButton backButton = new JButton("Back");
    private JButton computeButton = new JButton("Compute");
    private JButton sampleButton = new JButton("Load Sample Data");
    private JButton clearButton = new JButton("Reset / Clear");

    // input table: the Process column cannot be edited, only Arrival and Burst
    private DefaultTableModel inputModel = new DefaultTableModel(new String[] {"Process", "Arrival", "Burst"}, 0) {
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    };
    private JTable inputTable = new JTable(inputModel);

    // outputs
    private DefaultTableModel resultModel = new DefaultTableModel(new String[] {
            "Process", "Arrival", "Burst", "Completion", "Turnaround", "Waiting", "Response"}, 0);
    private JTable resultTable = new JTable(resultModel);
    private GanttPanel gantt = new GanttPanel();
    private JTextArea summary = new JTextArea(4, 40);

    public CpuPanel(String title, CpuScheduler scheduler, boolean usesQuantum,
                    int[] sampleArrival, int[] sampleBurst, int sampleQuantum) {
        this.scheduler = scheduler;
        this.usesQuantum = usesQuantum;
        this.sampleArrival = sampleArrival;
        this.sampleBurst = sampleBurst;
        this.sampleQuantum = sampleQuantum;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // top part: title row and input row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleRow.add(backButton);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleRow.add(titleLabel);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputRow.add(new JLabel("No. of processes:"));
        inputRow.add(countSpinner);
        if (usesQuantum) {
            inputRow.add(new JLabel("Time quantum:"));
            inputRow.add(quantumField);
        }
        inputRow.add(computeButton);
        inputRow.add(sampleButton);
        inputRow.add(clearButton);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(titleRow);
        top.add(inputRow);
        add(top, BorderLayout.NORTH);

        // center the numbers inside both tables
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        inputTable.setDefaultRenderer(Object.class, center);
        resultTable.setDefaultRenderer(Object.class, center);

        // left part: input table
        JScrollPane inputScroll = new JScrollPane(inputTable);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Input"));
        inputScroll.setPreferredSize(new Dimension(250, 0));
        add(inputScroll, BorderLayout.WEST);

        // middle part: result table on top, gantt chart below
        resultTable.setDefaultEditor(Object.class, null); // makes the result table read-only
        JScrollPane resultScroll = new JScrollPane(resultTable);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Results"));
        gantt.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        JPanel middle = new JPanel(new GridLayout(2, 1, 10, 10));
        middle.add(resultScroll);
        middle.add(gantt);
        add(middle, BorderLayout.CENTER);

        // bottom part: averages, utilization and throughput
        summary.setEditable(false);
        summary.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane summaryScroll = new JScrollPane(summary);
        summaryScroll.setBorder(BorderFactory.createTitledBorder("Summary"));
        add(summaryScroll, BorderLayout.SOUTH);

        // buttons and spinner call actionPerformed / stateChanged below
        backButton.addActionListener(this);
        computeButton.addActionListener(this);
        sampleButton.addActionListener(this);
        clearButton.addActionListener(this);
        countSpinner.addChangeListener(this);

        setRows(5);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            Main.showScreen("menu");
        } else if (e.getSource() == computeButton) {
            compute();
        } else if (e.getSource() == sampleButton) {
            loadSample();
        } else if (e.getSource() == clearButton) {
            clearAll();
        }
    }

    // the number of processes changed, so change the number of rows
    public void stateChanged(ChangeEvent e) {
        setRows((Integer) countSpinner.getValue());
    }

    private void setRows(int n) {
        // stop typing in a cell first, so the table can change safely
        if (inputTable.isEditing()) {
            inputTable.getCellEditor().cancelCellEditing();
        }
        inputModel.setRowCount(n);
        for (int i = 0; i < n; i++) {
            inputModel.setValueAt("P" + (i + 1), i, 0);
        }
    }

    private void compute() {
        // save the cell the user is still typing in (if they did not press Enter)
        if (inputTable.isEditing()) {
            inputTable.getCellEditor().stopCellEditing();
        }

        // read and check every input, stop at the first wrong one
        int n = inputModel.getRowCount();
        int[] arrival = new int[n];
        int[] burst = new int[n];
        for (int i = 0; i < n; i++) {
            arrival[i] = InputCheck.readNumber(this, inputModel.getValueAt(i, 1), "Arrival time of P" + (i + 1), 0, 999);
            if (arrival[i] == -1) {
                return;
            }
            burst[i] = InputCheck.readNumber(this, inputModel.getValueAt(i, 2), "Burst time of P" + (i + 1), 1, 999);
            if (burst[i] == -1) {
                return;
            }
        }

        int quantum = 0;
        if (usesQuantum) {
            quantum = InputCheck.readNumber(this, quantumField.getText(), "Time quantum", 1, 100);
            if (quantum == -1) {
                return;
            }
        }

        // run the algorithm, then show everything
        CpuResult result = scheduler.schedule(arrival, burst, quantum);

        resultModel.setRowCount(0);
        for (int i = 0; i < n; i++) {
            resultModel.addRow(new Object[] {"P" + (i + 1), arrival[i], burst[i], result.completion[i],
                    result.turnaround[i], result.waiting[i], result.response[i]});
        }

        gantt.setResult(result);

        summary.setText(
                "Average Turnaround Time : " + String.format("%.2f", CpuResult.average(result.turnaround)) + " ms\n"
                + "Average Waiting Time    : " + String.format("%.2f", CpuResult.average(result.waiting)) + " ms\n"
                + "Average Response Time   : " + String.format("%.2f", CpuResult.average(result.response)) + " ms\n"
                + "CPU Utilization         : " + String.format("%.2f", result.utilization()) + " %"
                + "  (" + result.busyTime() + " / " + result.totalTime() + ")     "
                + "Throughput: " + String.format("%.4f", result.throughput()) + " processes/ms"
                + "  (" + n + " / " + result.totalTime() + ")");
    }

    private void loadSample() {
        countSpinner.setValue(sampleArrival.length);
        setRows(sampleArrival.length);
        for (int i = 0; i < sampleArrival.length; i++) {
            inputModel.setValueAt(sampleArrival[i], i, 1);
            inputModel.setValueAt(sampleBurst[i], i, 2);
        }
        if (usesQuantum) {
            quantumField.setText("" + sampleQuantum);
        }
    }

    private void clearAll() {
        setRows(inputModel.getRowCount());
        for (int i = 0; i < inputModel.getRowCount(); i++) {
            inputModel.setValueAt("", i, 1);
            inputModel.setValueAt("", i, 2);
        }
        quantumField.setText("");
        resultModel.setRowCount(0);
        gantt.setResult(null);
        summary.setText("");
    }
}

// DiskPanel.java
// The screen for the disk scheduling algorithms (used by both SCAN and C-LOOK).
// Request table + settings on top, step table, head path graph and totals below.
// Written by: Nicholo Castor

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

public class DiskPanel extends JPanel implements ActionListener, ChangeListener {

    // the algorithm this screen uses (SCAN or C-LOOK)
    private DiskScheduler scheduler;
    private boolean sampleGoRight;

    // inputs
    private JSpinner countSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 50, 1));
    private JTextField headField = new JTextField(5);
    private JTextField sizeField = new JTextField("200", 5);
    private JComboBox<String> directionBox = new JComboBox<String>(new String[] {
            "Right (up)", "Left (to 0)"});
    private JButton backButton = new JButton("Back");
    private JButton computeButton = new JButton("Compute");
    private JButton sampleButton = new JButton("Load Sample Data");
    private JButton clearButton = new JButton("Reset / Clear");

    // request table: the Request # column cannot be edited, only Cylinder
    private DefaultTableModel requestModel = new DefaultTableModel(new String[] {"Request #", "Cylinder"}, 0) {
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    };
    private JTable requestTable = new JTable(requestModel);

    // outputs
    private DefaultTableModel stepModel = new DefaultTableModel(new String[] {"Step", "From", "To", "Movement"}, 0);
    private JTable stepTable = new JTable(stepModel);
    private HeadPathPanel graph = new HeadPathPanel();
    private JTextArea summary = new JTextArea(3, 40);

    public DiskPanel(String title, DiskScheduler scheduler, boolean sampleGoRight) {
        this.scheduler = scheduler;
        this.sampleGoRight = sampleGoRight;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // top part: title row and input row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleRow.add(backButton);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleRow.add(titleLabel);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputRow.add(new JLabel("No. of requests:"));
        inputRow.add(countSpinner);
        inputRow.add(new JLabel("Initial head:"));
        inputRow.add(headField);
        inputRow.add(new JLabel("Disk size:"));
        inputRow.add(sizeField);
        inputRow.add(new JLabel("Direction:"));
        inputRow.add(directionBox);
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
        requestTable.setDefaultRenderer(Object.class, center);
        stepTable.setDefaultRenderer(Object.class, center);

        // left part: request table on top, step table below
        stepTable.setDefaultEditor(Object.class, null); // makes the step table read-only
        JScrollPane requestScroll = new JScrollPane(requestTable);
        requestScroll.setBorder(BorderFactory.createTitledBorder("Requests"));
        JScrollPane stepScroll = new JScrollPane(stepTable);
        stepScroll.setBorder(BorderFactory.createTitledBorder("Steps"));
        JPanel leftSide = new JPanel(new GridLayout(2, 1, 10, 10));
        leftSide.add(requestScroll);
        leftSide.add(stepScroll);
        leftSide.setPreferredSize(new Dimension(300, 0));
        add(leftSide, BorderLayout.WEST);

        // middle part: head path graph
        graph.setBorder(BorderFactory.createTitledBorder("Head Movement Graph"));
        add(graph, BorderLayout.CENTER);

        // bottom part: seek sequence and totals
        summary.setEditable(false);
        summary.setLineWrap(true);
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

        setRows(8);
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

    // the number of requests changed, so change the number of rows
    public void stateChanged(ChangeEvent e) {
        setRows((Integer) countSpinner.getValue());
    }

    private void setRows(int n) {
        // stop typing in a cell first, so the table can change safely
        if (requestTable.isEditing()) {
            requestTable.getCellEditor().cancelCellEditing();
        }
        requestModel.setRowCount(n);
        for (int i = 0; i < n; i++) {
            requestModel.setValueAt(i + 1, i, 0);
        }
    }

    private void compute() {
        // save the cell the user is still typing in (if they did not press Enter)
        if (requestTable.isEditing()) {
            requestTable.getCellEditor().stopCellEditing();
        }

        // disk size first, because head and requests must fit inside the disk
        int diskSize = InputCheck.readNumber(this, sizeField.getText(), "Disk size", 2, 10000);
        if (diskSize == -1) {
            return;
        }
        int head = InputCheck.readNumber(this, headField.getText(), "Initial head", 0, diskSize - 1);
        if (head == -1) {
            return;
        }

        int n = requestModel.getRowCount();
        int[] requests = new int[n];
        for (int i = 0; i < n; i++) {
            requests[i] = InputCheck.readNumber(this, requestModel.getValueAt(i, 1), "Request #" + (i + 1), 0, diskSize - 1);
            if (requests[i] == -1) {
                return;
            }
        }

        boolean goRight = directionBox.getSelectedIndex() == 0;

        // run the algorithm, then show everything
        DiskResult result = scheduler.schedule(requests, head, diskSize, goRight);

        stepModel.setRowCount(0);
        String sequence = "" + result.path[0];
        for (int i = 1; i < result.count; i++) {
            stepModel.addRow(new Object[] {i, result.path[i - 1], result.path[i], result.movement(i)});
            sequence = sequence + " -> " + result.path[i];
        }

        graph.setResult(result, diskSize);

        summary.setText(
                "Seek sequence          : " + sequence + "\n"
                + "Total head movement    : " + result.totalMovement() + " cylinders\n"
                + "Average seek distance  : " + String.format("%.2f", result.averageSeek(n)) + " cylinders");
    }

    private void loadSample() {
        int[] sample = SampleData.DISK_REQUESTS;
        countSpinner.setValue(sample.length);
        setRows(sample.length);
        for (int i = 0; i < sample.length; i++) {
            requestModel.setValueAt(sample[i], i, 1);
        }
        headField.setText("" + SampleData.DISK_HEAD);
        sizeField.setText("" + SampleData.DISK_SIZE);
        if (sampleGoRight) {
            directionBox.setSelectedIndex(0);
        } else {
            directionBox.setSelectedIndex(1);
        }
    }

    private void clearAll() {
        setRows(requestModel.getRowCount());
        for (int i = 0; i < requestModel.getRowCount(); i++) {
            requestModel.setValueAt("", i, 1);
        }
        headField.setText("");
        sizeField.setText("200");
        directionBox.setSelectedIndex(0);
        stepModel.setRowCount(0);
        graph.setResult(null, 200);
        summary.setText("");
    }
}

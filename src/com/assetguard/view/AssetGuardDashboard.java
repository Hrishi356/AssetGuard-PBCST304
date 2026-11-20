package com.assetguard.view;

import com.assetguard.dao.IncidentDAO;
import com.assetguard.model.Incident;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssetGuardDashboard extends JFrame {
    private final IncidentDAO incidentDAO = new IncidentDAO();
    private JLabel lblTotalCount, lblOpenCount, lblProgressCount, lblResolvedCount;
    private JComboBox<String> cmbAssetId, cmbCategory, cmbSeverity, cmbFilterStatus, cmbFilterSeverity;
    private JTextField txtReporter;
    private JTextArea txtDescription;
    private JTable incidentTable;
    private DefaultTableModel tableModel;

    public AssetGuardDashboard() {
        setTitle("AssetGuard - Centralized Incident Management System (PBCST304)");
        setSize(1150, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(createTopKpiPanel(), BorderLayout.NORTH);
        add(createMainWorkspacePanel(), BorderLayout.CENTER);
        refreshDashboardData();
    }

    private JPanel createTopKpiPanel() {
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        kpiPanel.setBorder(new EmptyBorder(12, 15, 5, 15));

        lblTotalCount = new JLabel("0", SwingConstants.CENTER);
        lblOpenCount = new JLabel("0", SwingConstants.CENTER);
        lblProgressCount = new JLabel("0", SwingConstants.CENTER);
        lblResolvedCount = new JLabel("0", SwingConstants.CENTER);

        kpiPanel.add(buildCard("TOTAL INCIDENTS", lblTotalCount, new Color(41, 128, 185)));
        kpiPanel.add(buildCard("OPEN FAULTS", lblOpenCount, new Color(192, 57, 43)));
        kpiPanel.add(buildCard("IN PROGRESS", lblProgressCount, new Color(211, 84, 0)));
        kpiPanel.add(buildCard("RESOLVED", lblResolvedCount, new Color(39, 174, 96)));
        return kpiPanel;
    }

    private JPanel buildCard(String title, JLabel valueLabel, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JSplitPane createMainWorkspacePanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createReportFormPanel(), createAdminTablePanel());
        splitPane.setDividerLocation(360);
        splitPane.setBorder(new EmptyBorder(5, 15, 15, 15));
        return splitPane;
    }

    private JPanel createReportFormPanel() {
        JPanel panel = new JPanel(new GridLayout(11, 1, 5, 5));
        panel.setBorder(new TitledBorder("Log New Equipment Fault"));

        cmbAssetId = new JComboBox<>(new String[]{"CSLAB1-PC12", "MP-ARD-08", "NET-SW-02", "CSLAB2-KB05"});
        txtReporter = new JTextField("hrishikesh");
        cmbCategory = new JComboBox<>(new String[]{
                "Boot Failure / No Display", "Defective Microcontroller IC",
                "Packet Loss / Switch Fault", "Peripheral Fault"
        });
        cmbSeverity = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});
        txtDescription = new JTextArea(3, 20);

        JButton btnSubmit = new JButton("Submit Fault Report");
        btnSubmit.addActionListener(e -> handleCreateIncident());

        panel.add(new JLabel("Asset ID:"));
        panel.add(cmbAssetId);
        panel.add(new JLabel("Reported By:"));
        panel.add(txtReporter);
        panel.add(new JLabel("Issue Category:"));
        panel.add(cmbCategory);
        panel.add(new JLabel("Severity Level:"));
        panel.add(cmbSeverity);
        panel.add(new JLabel("Fault Description:"));
        panel.add(new JScrollPane(txtDescription));
        panel.add(btnSubmit);
        return panel;
    }

    private JPanel createAdminTablePanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBorder(new TitledBorder("Administrative Incident Ledger"));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbFilterStatus = new JComboBox<>(new String[]{"ALL", "OPEN", "IN_PROGRESS", "RESOLVED"});
        cmbFilterSeverity = new JComboBox<>(new String[]{"ALL", "LOW", "MEDIUM", "HIGH", "CRITICAL"});
        JButton btnFilter = new JButton("Apply Filter");
        btnFilter.addActionListener(e -> refreshDashboardData());

        filterBar.add(new JLabel("Status:"));
        filterBar.add(cmbFilterStatus);
        filterBar.add(new JLabel("Severity:"));
        filterBar.add(cmbFilterSeverity);
        filterBar.add(btnFilter);

        String[] cols = {"ID", "Asset ID", "Reporter", "Category", "Severity", "Status", "Remarks", "Reported At"};
        tableModel = new DefaultTableModel(cols, 0);
        incidentTable = new JTable(tableModel);

        JButton btnUpdate = new JButton("Update Selected Incident Status");
        btnUpdate.addActionListener(e -> handleUpdateStatus());

        rightPanel.add(filterBar, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(incidentTable), BorderLayout.CENTER);
        rightPanel.add(btnUpdate, BorderLayout.SOUTH);
        return rightPanel;
    }

    private void handleCreateIncident() {
        if (txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a clear fault description.");
            return;
        }
        Incident inc = new Incident(
                (String) cmbAssetId.getSelectedItem(),
                txtReporter.getText().trim(),
                (String) cmbCategory.getSelectedItem(),
                txtDescription.getText().trim(),
                (String) cmbSeverity.getSelectedItem()
        );
        if (incidentDAO.createIncident(inc)) {
            txtDescription.setText("");
            refreshDashboardData();
            JOptionPane.showMessageDialog(this, "Incident logged successfully!");
        }
    }

    private void handleUpdateStatus() {
        int row = incidentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an incident from the table first.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String[] statuses = {"OPEN", "IN_PROGRESS", "RESOLVED"};
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select New Status:",
                "Update Incident #" + id, JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[1]);
        if (newStatus != null) {
            String remarks = JOptionPane.showInputDialog(this, "Enter Technician Remarks:", "Resolved / Checked");
            incidentDAO.updateIncidentStatus(id, newStatus, remarks != null ? remarks : "");
            refreshDashboardData();
        }
    }

    private void refreshDashboardData() {
        int[] counts = incidentDAO.getDashboardCounts();
        lblTotalCount.setText(String.valueOf(counts[0]));
        lblOpenCount.setText(String.valueOf(counts[1]));
        lblProgressCount.setText(String.valueOf(counts[2]));
        lblResolvedCount.setText(String.valueOf(counts[3]));

        tableModel.setRowCount(0);
        List<Incident> list = incidentDAO.getIncidentsByFilter(
                (String) cmbFilterStatus.getSelectedItem(),
                (String) cmbFilterSeverity.getSelectedItem()
        );
        for (Incident i : list) {
            tableModel.addRow(new Object[]{
                    i.getIncidentId(), i.getAssetId(), i.getReportedBy(),
                    i.getIssueCategory(), i.getSeverity(), i.getStatus(),
                    i.getAdminRemarks(), i.getReportedAt()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssetGuardDashboard().setVisible(true));
    }
}

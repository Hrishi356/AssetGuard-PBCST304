package com.assetguard.dao;

import com.assetguard.model.Incident;
import com.assetguard.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO {

    public boolean createIncident(Incident incident) {
        String sql = "INSERT INTO incidents (asset_id, reported_by, issue_category, description, severity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, incident.getAssetId());
            pstmt.setString(2, incident.getReportedBy());
            pstmt.setString(3, incident.getIssueCategory());
            pstmt.setString(4, incident.getDescription());
            pstmt.setString(5, incident.getSeverity());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating incident: " + e.getMessage());
            return false;
        }
    }

    public List<Incident> getIncidentsByFilter(String statusFilter, String severityFilter) {
        List<Incident> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM incidents WHERE 1=1");
        if (!"ALL".equalsIgnoreCase(statusFilter)) sql.append(" AND status = ?");
        if (!"ALL".equalsIgnoreCase(severityFilter)) sql.append(" AND severity = ?");
        sql.append(" ORDER BY incident_id DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (!"ALL".equalsIgnoreCase(statusFilter)) pstmt.setString(idx++, statusFilter);
            if (!"ALL".equalsIgnoreCase(severityFilter)) pstmt.setString(idx, severityFilter);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Incident(
                            rs.getInt("incident_id"),
                            rs.getString("asset_id"),
                            rs.getString("reported_by"),
                            rs.getString("issue_category"),
                            rs.getString("description"),
                            rs.getString("severity"),
                            rs.getString("status"),
                            rs.getString("admin_remarks"),
                            rs.getString("reported_at"),
                            rs.getString("resolved_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching incidents: " + e.getMessage());
        }
        return list;
    }

    public boolean updateIncidentStatus(int incidentId, String newStatus, String remarks) {
        String sql = "RESOLVED".equalsIgnoreCase(newStatus)
                ? "UPDATE incidents SET status = ?, admin_remarks = ?, resolved_at = CURRENT_TIMESTAMP WHERE incident_id = ?"
                : "UPDATE incidents SET status = ?, admin_remarks = ?, resolved_at = NULL WHERE incident_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, remarks);
            pstmt.setInt(3, incidentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    public int[] getDashboardCounts() {
        int[] counts = new int[4];
        String sql = "SELECT status, COUNT(*) as cnt FROM incidents GROUP BY status";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String st = rs.getString("status");
                int c = rs.getInt("cnt");
                counts[0] += c;
                if ("OPEN".equalsIgnoreCase(st)) counts[1] = c;
                else if ("IN_PROGRESS".equalsIgnoreCase(st)) counts[2] = c;
                else if ("RESOLVED".equalsIgnoreCase(st)) counts[3] = c;
            }
        } catch (SQLException e) {
            System.err.println("Error computing dashboard metrics: " + e.getMessage());
        }
        return counts;
    }
}

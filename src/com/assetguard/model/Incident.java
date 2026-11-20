package com.assetguard.model;

public class Incident {
    private int incidentId;
    private String assetId;
    private String reportedBy;
    private String issueCategory;
    private String description;
    private String severity;
    private String status;
    private String adminRemarks;
    private String reportedAt;
    private String resolvedAt;

    public Incident() {}

    public Incident(String assetId, String reportedBy, String issueCategory, String description, String severity) {
        this.assetId = assetId;
        this.reportedBy = reportedBy;
        this.issueCategory = issueCategory;
        this.description = description;
        this.severity = severity;
        this.status = "OPEN";
        this.adminRemarks = "Pending review";
    }

    public Incident(int incidentId, String assetId, String reportedBy, String issueCategory,
                    String description, String severity, String status, String adminRemarks,
                    String reportedAt, String resolvedAt) {
        this.incidentId = incidentId;
        this.assetId = assetId;
        this.reportedBy = reportedBy;
        this.issueCategory = issueCategory;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.adminRemarks = adminRemarks;
        this.reportedAt = reportedAt;
        this.resolvedAt = resolvedAt;
    }

    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int incidentId) { this.incidentId = incidentId; }
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public String getIssueCategory() { return issueCategory; }
    public void setIssueCategory(String issueCategory) { this.issueCategory = issueCategory; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }
    public String getReportedAt() { return reportedAt; }
    public void setReportedAt(String reportedAt) { this.reportedAt = reportedAt; }
    public String getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(String resolvedAt) { this.resolvedAt = resolvedAt; }
}

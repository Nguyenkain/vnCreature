package com.example.vncreatures.model.discussion;

public class Report {
    private String report_type_id = null;
    private String report_type = null;
    private String report_id = null;
    private String thread_id = null;
    private String user_id = null;
    private String comment = null;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReport_type_id() {
        return report_type_id;
    }

    public void setReport_type_id(String report_type_id) {
        this.report_type_id = report_type_id;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    @Override
    public String toString() {
        return report_type;
    }
}

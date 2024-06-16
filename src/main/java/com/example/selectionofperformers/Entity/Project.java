package com.example.selectionofperformers.Entity;

public class Project {
    private String idProject;
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private String startDate;
    private String endDate;

    public Project(String idProject, String projectName, String projectDescription, String projectStatus, String startDate, String endDate) {
        this.idProject = idProject;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStatus = projectStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getProjectId() {
        return idProject ;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectStatus() {
        return projectStatus;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setProjectId() {
        this.idProject = idProject;
    }

    public void setProjectName() {
        this.projectName = projectName;
    }

    public void setProjectDescription() {
        this.projectDescription = projectDescription;
    }

    public void setProjectStatus() {
        this.projectStatus = projectStatus;
    }
    public void setStartDate() {
        this.startDate = startDate;
    }
    public void setEndDate() {
        this.endDate = endDate;
    }
}

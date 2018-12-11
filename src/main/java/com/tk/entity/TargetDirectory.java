package com.tk.entity;

/**
 * Created by tanke on 2018/12/10.
 */
public class TargetDirectory {

    /**
     * 项目目录
     */
    private String projectName;

    /**
     * pojo包目录
     */
    private String packagePoName;

    /**
     * pojo基础包目录
     */
    private String basePackagePoName;

    /**
     * dao包目录
     */
    private String packageDaoName;

    /**
     * pojo基础包目录
     */
    private String basePackageDaoName;

    /**
     * xml包目录
     */
    private String packageMapperXmlName;

    /**
     * xml基础包目录
     */
    private String basePackageMapperXmlName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPackagePoName() {
        return packagePoName;
    }

    public void setPackagePoName(String packagePoName) {
        this.packagePoName = packagePoName;
    }

    public String getBasePackagePoName() {
        return basePackagePoName;
    }

    public void setBasePackagePoName(String basePackagePoName) {
        this.basePackagePoName = basePackagePoName;
    }

    public String getPackageDaoName() {
        return packageDaoName;
    }

    public void setPackageDaoName(String packageDaoName) {
        this.packageDaoName = packageDaoName;
    }

    public String getBasePackageDaoName() {
        return basePackageDaoName;
    }

    public void setBasePackageDaoName(String basePackageDaoName) {
        this.basePackageDaoName = basePackageDaoName;
    }

    public String getPackageMapperXmlName() {
        return packageMapperXmlName;
    }

    public void setPackageMapperXmlName(String packageMapperXmlName) {
        this.packageMapperXmlName = packageMapperXmlName;
    }

    public String getBasePackageMapperXmlName() {
        return basePackageMapperXmlName;
    }

    public void setBasePackageMapperXmlName(String basePackageMapperXmlName) {
        this.basePackageMapperXmlName = basePackageMapperXmlName;
    }

    @Override
    public String toString() {
        return "TargetDirectory{" +
                "projectName='" + projectName + '\'' +
                ", packagePoName='" + packagePoName + '\'' +
                ", basePackagePoName='" + basePackagePoName + '\'' +
                ", packageDaoName='" + packageDaoName + '\'' +
                ", basePackageDaoName='" + basePackageDaoName + '\'' +
                ", packageMapperXmlName='" + packageMapperXmlName + '\'' +
                ", basePackageMapperXmlName='" + basePackageMapperXmlName + '\'' +
                '}';
    }
}

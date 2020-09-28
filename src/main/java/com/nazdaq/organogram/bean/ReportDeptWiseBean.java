package com.nazdaq.organogram.bean;

public class ReportDeptWiseBean {
	
	private String deptName;
	private String deptHeadName;
	
	public ReportDeptWiseBean() {
		super();
	}
	public ReportDeptWiseBean(String deptName, String deptHeadName) {
		super();
		this.deptName = deptName;
		this.deptHeadName = deptHeadName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptHeadName() {
		return deptHeadName;
	}
	public void setDeptHeadName(String deptHeadName) {
		this.deptHeadName = deptHeadName;
	}
	
}

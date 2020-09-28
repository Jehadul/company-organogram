package com.nazdaq.organogram.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;


/**
 * @author RAFIQUL
 *
 */
public class JrxmToJasper {
	public static void main(String[] args) throws JRException {
		// TODO Auto-generated method stub
		
        JasperCompileManager.compileReportToFile(
        		"E:\\git-workspace\\sts_390\\synergy-ejms\\src\\main\\resources\\CandidateProfile.jrxml", 
        		"E:\\git-workspace\\sts_390\\synergy-ejms\\src\\main\\resources\\CandidateProfile.jasper");
     }

}

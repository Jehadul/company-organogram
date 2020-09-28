package com.nazdaq.organogram;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.organogram.bean.CommonNameBean;
import com.nazdaq.organogram.bean.OgrSynBean;
import com.nazdaq.organogram.bean.ReportDeptWiseBean;
import com.nazdaq.organogram.model.Department;
import com.nazdaq.organogram.model.Employee;
import com.nazdaq.organogram.model.Point;
import com.nazdaq.organogram.service.CommonService;
import com.nazdaq.organogram.util.Constants;

import javafx.scene.control.Accordion;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

@Controller
public class ReportController implements Constants {

	@Autowired
	private CommonService commonService;

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/downLoadsynergyReport")
	public ModelAndView generateSynergyAllReportPdf(ModelAndView modelAndView) {
		logger.debug("--------------generate PDF report----------");
		Map<String, Object> parameterMap = new HashMap<String, Object>();

		List<OgrSynBean> ogrSynBeans = new ArrayList<>();
		List<Department> departments = (List<Department>) (Object) commonService.getAllObjectList("Department");
		OgrSynBean ogrSynBeanObj = new OgrSynBean();

		for (Department department : departments) {
			String departmentHead = "N/A";
			String designation = "";
		
			if (department.getDeptHead().getDesignation() != null)
				{
					designation = department.getDeptHead().getDesignation().getName();
					if(designation.trim().equals("&nbsp;")){
						designation = " N/A";
					}
				}
				
			if (department.getDeptHead() != null) {
				departmentHead = ""+designation + "<br>" + department.getDeptHead().getName() + "";
			} 
			if (department.getDeptKeyword() != null && department.getBusinessType() != null
					&& department.getBusinessType().toString().trim().equals(BUSINESS_TYPE.TRADE.toString().trim())) {

				if (department.getDeptKeyword().trim().equals(SALES)) {
					ogrSynBeanObj.setHeadSales(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(ACCOUNTS)) {
					ogrSynBeanObj.setHeadAccount(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(ASSETS)) {
					ogrSynBeanObj.setHeadAssets(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(CUSTOMER_SERVICE)) {
					ogrSynBeanObj.setHeadCustomer(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(HR_ADMIN)) {
					ogrSynBeanObj.setHeadHrAdmin(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(IMPORT_EXPORT)) {
					ogrSynBeanObj.setHeadImportExport(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(OPERATION.trim().toString())) {
					ogrSynBeanObj.setHeadOperation(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(PROCUREMENT)) {
					ogrSynBeanObj.setHeadProc(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(PRODUCT_SOURCING)) {
					ogrSynBeanObj.setHeadProductSouring(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(STORE_LOGISTIC)) {
					ogrSynBeanObj.setHeadStoreLogistic(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(TENDER)) {
					ogrSynBeanObj.setHeadTender(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(VAT_TAX)) {
					ogrSynBeanObj.setHeadVatTax(departmentHead);
				}
			}
			if (department.getDeptKeyword() != null && department.getBusinessType() != null && department
					.getBusinessType().toString().trim().equals(BUSINESS_TYPE.EMERGING.toString().trim())) {

				if (department.getDeptKeyword().trim().equals(SALES.trim())) {
					ogrSynBeanObj.setEmergingHeadSales(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(PROCUREMENT)) {
					ogrSynBeanObj.setEmergingHeadProcurment(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(PRODUCT_SOURCING)) {
					ogrSynBeanObj.setEmergingHeadProduct(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(ACCOUNTS)) {
					ogrSynBeanObj.setEmergingHeadAccount(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(STORE_LOGISTIC)) {
					ogrSynBeanObj.setEmergingHeadStoreLogistic(departmentHead);
				}
				if (department.getDeptKeyword().trim().equals(PROJECT_IMPL)) {
					ogrSynBeanObj.setEmergingHeadProject(departmentHead);
				}

			}

		}
		ogrSynBeans.add(ogrSynBeanObj);

		List<Employee> emList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee", "status",
				"1");
		List<CommonNameBean> govtSalesList = new ArrayList<>();
		List<CommonNameBean> privateSalesList = new ArrayList<>();
		List<CommonNameBean> accountlsEmergingList = new ArrayList<>();
		List<CommonNameBean> procurmentsList = new ArrayList<>();

		for (Department department : departments) {
			if (department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim())
					&& department.getDeptKeyword().equals(GOVT_SALES.toString().trim())) {
				// List<Employee> empList = (List<Employee>) (Object)
				// commonService.getObjectListByAnyColumn("Employee", "subDeptKeyword",
				// GOVT_SALES);
				// List<String> govtSales = new ArrayList<String>();
				for (Employee employee : emList) {
					if (employee.getSubDepartment() != null
							&& employee.getSubDepartment().getDeptKeyword().equals(GOVT_SALES.toString().trim())) {
						CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
						govtSalesList.add(commonNameBeanGovtSales);
					}

				}
				// db.setGovtSales(govtSales);
			}
			if (department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim())
					&& department.getDeptKeyword().equals(PRIVATE_SALES.toString().trim())) {

				// List<String> privateSales = new ArrayList<String>();
				for (Employee employee : emList) {
					if (employee.getSubDepartment() != null
							&& employee.getSubDepartment().getDeptKeyword().equals(PRIVATE_SALES.toString().trim())) {
						CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
						privateSalesList.add(commonNameBeanGovtSales);
					}

				}
				// db.setPrivateSales(privateSales);
			}
			if (department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim())
					&& department.getDeptKeyword().equals(ACCOUNTS.toString().trim())) {

				// List<String> privateSales = new ArrayList<String>();
				for (Employee employee : emList) {
					if (employee.getDepartment() != null
							&& employee.getDepartment().getDeptKeyword().equals(ACCOUNTS.toString().trim())) {
						CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
						accountlsEmergingList.add(commonNameBeanGovtSales);
					}

				}
				// db.setPrivateSales(privateSales);
			}
			if (department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim())
					&& department.getDeptKeyword().equals(PROCUREMENT.toString().trim())) {

				// List<String> privateSales = new ArrayList<String>();
				for (Employee employee : emList) {
					if (employee.getDepartment() != null
							&& employee.getDepartment().getDeptKeyword().equals(PROCUREMENT.toString().trim())) {
						CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
						procurmentsList.add(commonNameBeanGovtSales);
					}

				}
				// db.setPrivateSales(privateSales);
			}
		}

		JRDataSource JRdataSource = new JRBeanCollectionDataSource(ogrSynBeans);

		parameterMap.put("datasource", JRdataSource);
		parameterMap.put("SUBREPORT_DATA_GOVT", govtSalesList);
		parameterMap.put("SUBREPORT_DATA_PRIVATE", privateSalesList);
		parameterMap.put("SUBREPORT_DATA_EMERGING", accountlsEmergingList);
		parameterMap.put("SUBREPORT_DATA_PROCURMENT", procurmentsList);

		modelAndView = new ModelAndView("synergyAllReportPdf", parameterMap);
		return modelAndView;
	}

	/*
	 * @RequestMapping(method = RequestMethod.GET, value = "/downLoadsynergyReport")
	 * public ModelAndView generateSynergyAllReportPdf(ModelAndView modelAndView) {
	 * logger.debug("--------------generate PDF report----------"); Map<String,
	 * Object> parameterMap = new HashMap<String, Object>();
	 * 
	 * JRDataSource JRdataSource = new JRBeanCollectionDataSource(ogrSynBeans);
	 * 
	 * parameterMap.put("datasource", JRdataSource); modelAndView = new
	 * ModelAndView("synergyAllReportPdf", parameterMap); return modelAndView; }
	 */

	@SuppressWarnings("unchecked")
	// @RequestMapping(value = "/downLoadsynergyReport", method = RequestMethod.GET)
	@ResponseBody
	public void downLoadDlvReport(HttpServletRequest request, HttpServletResponse response)
			throws JRException, IOException {

		List<OgrSynBean> ogrSynBeans = new ArrayList<>();
		List<Department> departments = (List<Department>) (Object) commonService.getAllObjectList("Department");
		OgrSynBean ogrSynBeanObj = new OgrSynBean();

		for (Department department : departments) {
			if (department.getBusinessType() != null
					&& department.getBusinessType().toString().trim().equals(BUSINESS_TYPE.TRADE.toString().trim())) {
				if (department.getDeptKeyword().trim().equals(SALES)) {
					ogrSynBeanObj.setHeadSales(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(ACCOUNTS)) {
					ogrSynBeanObj.setHeadAccount(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(ASSETS)) {
					ogrSynBeanObj.setHeadAssets(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(CUSTOMER_SERVICE)) {
					ogrSynBeanObj.setHeadCustomer(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(HR_ADMIN)) {
					ogrSynBeanObj.setHeadHrAdmin(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(IMPORT_EXPORT)) {
					ogrSynBeanObj.setHeadImportExport(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(OPERATION)) {
					ogrSynBeanObj.setHeadOperation(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(PROCUREMENT)) {
					ogrSynBeanObj.setHeadProc(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(PRODUCT_SOURCING)) {
					ogrSynBeanObj.setHeadProductSouring(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(STORE_LOGISTIC)) {
					ogrSynBeanObj.setHeadStoreLogistic(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(TENDER)) {
					ogrSynBeanObj.setHeadTender(department.getDeptHead().getName());
				}
				if (department.getDeptKeyword().trim().equals(VAT_TAX)) {
					ogrSynBeanObj.setHeadVatTax(department.getDeptHead().getName());
				}
			}

		}
		ogrSynBeans.add(ogrSynBeanObj);

		List<Employee> emList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee", "status",
				"1");
		List<CommonNameBean> govtSalesList = new ArrayList<>();
		List<CommonNameBean> privateSalesList = new ArrayList<>();
		List<CommonNameBean> accountlsEmergingList = new ArrayList<>();
		List<CommonNameBean> procurmentsList = new ArrayList<>();

		for (Employee employee : emList) {
			if (employee.getDepartment().getBusinessType().toString().trim()
					.equals(BUSINESS_TYPE.TRADE.toString().trim())) {
				if (employee.getDepartment().getDeptKeyword().toString().trim().equals(GOVT_SALES.toString().trim())) {
					CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
					govtSalesList.add(commonNameBeanGovtSales);
				}
				if (employee.getDepartment().getDeptKeyword().toString().trim()
						.equals(PRIVATE_SALES.toString().trim())) {
					CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
					privateSalesList.add(commonNameBeanGovtSales);
				}
			}
			if (employee.getDepartment().getBusinessType().toString().trim()
					.equals(BUSINESS_TYPE.EMERGING.toString().trim())) {
				if (employee.getDepartment().getDeptKeyword().toString().trim().equals(ACCOUNTS.toString().trim())) {
					CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
					accountlsEmergingList.add(commonNameBeanGovtSales);
				}
				if (employee.getDepartment().getDeptKeyword().toString().trim().equals(PROCUREMENT.toString().trim())) {
					CommonNameBean commonNameBeanGovtSales = new CommonNameBean(employee.getName());
					procurmentsList.add(commonNameBeanGovtSales);
				}
			}

		}

		// main section
		JRDataSource jRdataSource = null;
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/report/organoGramSN.jasper");
		OgrSynBean ogrSynBean = new OgrSynBean();
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> datasourcemap = new HashMap<>();
		datasourcemap.put("ogrSynBean", ogrSynBean);
		jRdataSource = new JRBeanCollectionDataSource(ogrSynBeans, false);
		params.put("datasource", jRdataSource);
		params.put("SUBREPORT_DATA_GOVT", govtSalesList);
		params.put("SUBREPORT_DATA_PRIVATE", privateSalesList);
		params.put("SUBREPORT_DATA_EMERGING", accountlsEmergingList);
		params.put("SUBREPORT_DATA_PROCURMENT", procurmentsList);
		// prepare report first for one
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRdataSource);

		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=organogam_syn.pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}
@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = "/downLoadDeptWise")
	public ModelAndView generateDeptWiseReportPdf(ModelAndView modelAndView,
			@ModelAttribute("command") Department command, HttpServletRequest request, HttpServletResponse response)
			throws JRException, IOException {
		logger.debug("--------------generate PDF report----------");
		Map<String, Object> parameterMap = new HashMap<String, Object>();

		List<CommonNameBean> leftList = new ArrayList<>();
		List<CommonNameBean> rightList = new ArrayList<>();
		List<CommonNameBean> rightList_L = new ArrayList<>();
		List<CommonNameBean> rightList_R = new ArrayList<>();
		List<ReportDeptWiseBean> reportDeptList = new ArrayList<>();
		Department department = (Department) commonService.getAnObjectByAnyUniqueColumn("Department", "id",
				request.getParameter("id"));
		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
				"department_id", request.getParameter("id"), "status", "1");
		if(department.getDeptKeyword().toString().trim().equals(ADMIN.toString().trim()) || department.getDeptKeyword().toString().trim().equals(HR.toString().trim())){
			employeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
					"subDepartment_id", request.getParameter("id"), "status", "1");
		}
		
		Map<Integer, Integer> dupmap = new HashMap<>();
		if(department.getDeptKeyword() != null && department.getDeptKeyword().toString().trim().equals(HR_ADMIN.toString().trim())|| 
				department.getDeptKeyword().toString().trim().equals(CUSTOMER_SERVICE.toString().trim()) ||
				department.getDeptKeyword().toString().trim().equals(SALES.toString().trim()) ||
				department.getDeptKeyword().toString().trim().equals(GOVT_SALES.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(PRIVATE_SALES.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(PROCUREMENT.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(OPERATION.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(ACCOUNTS.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(IMPORT_EXPORT.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(STORE_LOGISTIC.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(PRODUCT_SOURCING.toString().trim())||
				department.getDeptKeyword().toString().trim().equals(TENDER.toString().trim()))
		{
			
			
			if (department.getDeptKeyword() != null
					&& department.getDeptKeyword().toString().trim().equals(HR_ADMIN.toString().trim())) {
				leftList.add(new CommonNameBean("Human Resource"));
				rightList.add(new CommonNameBean("Admin"));
				Department department2 = (Department) commonService.getAnObjectByAnyUniqueColumn("Department",
						"dept_keyword", "ADMIN");
				// rightList.add(new
				// CommonNameBean(department2.getDeptHead().getDesignation().getName() + "("+
				// department2.getDeptHead().getName() + ")"));
				dupmap.put(department2.getDeptHead().getId(), department2.getDeptHead().getId());
			}
			List<Employee> empUnderServiceHead = new ArrayList<>();
			List<Employee> empUnderManagerListLeft = new ArrayList<>();
			List<Employee> empUnderManagerListRighr = new ArrayList<>();
			if (department.getDeptKeyword().trim().equals(CUSTOMER_SERVICE.trim())) {
				empUnderServiceHead = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
						"department_id", department.getId().toString(), "manager_id",
						department.getDeptHead().getId().toString());
				leftList.add(new CommonNameBean(empUnderServiceHead.get(0).getDesignation().getName() + "("
						+ empUnderServiceHead.get(0).getName() + ")"));
				// rightList.add(new
				// CommonNameBean(empUnderServiceHead.get(1).getDesignation().getName() + "(" +
				// empUnderServiceHead.get(1).getName() + ")"));
				empUnderManagerListLeft = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
						"department_id", department.getId().toString(), "manager_id",
						empUnderServiceHead.get(0).getId().toString());
				empUnderManagerListRighr = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
						"department_id", department.getId().toString(), "manager_id",
						empUnderServiceHead.get(1).getId().toString());

				for (Employee employee : empUnderManagerListLeft) {
					leftList.add(new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				for (Employee employee : empUnderManagerListRighr) {
					if (employee.getEmpId().toString().trim().substring(0, 1).trim().toLowerCase().equals("s")) {
						rightList_L.add(
								new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (employee.getEmpId().toString().trim().substring(0, 1).trim().toLowerCase().equals("m")) {
						rightList_R.add(
								new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
				}

			}

			if (department.getDeptKeyword() != null && department.getDeptKeyword().trim().equals(GOVT_SALES.trim())
					|| department.getDeptKeyword().trim().equals(PRIVATE_SALES.trim())) {
				List<Employee> subemployeeList = (List<Employee>) (Object) commonService.getObjectListByTwoColumn(
						"Employee", "subDepartment_id", request.getParameter("id"), "status", "1");
				for (Employee employee : subemployeeList) {
					if (employee.getSubDepartment().getDeptKeyword() != null
							&& employee.getSubDepartment().getDeptKeyword().trim().equals(GOVT_SALES.trim())
							|| employee.getSubDepartment().getDeptKeyword().trim().equals(PRIVATE_SALES.trim())) {
						if (subemployeeList.size() / 2 <= subemployeeList.indexOf(employee)) {
							leftList.add(new CommonNameBean(
									employee.getDesignation().getName() + "(" + employee.getName() + ")"));
						}
						if (subemployeeList.size() / 2 > subemployeeList.indexOf(employee)) {
							rightList.add(new CommonNameBean(
									employee.getDesignation().getName() + "(" + employee.getName() + ")"));
						}
					} else {
						continue;
					}
				}
			}

			if (department.getDeptKeyword() != null
					&& department.getDeptKeyword().toString().trim().equals(SALES.toString().trim())) {
				leftList.add(new CommonNameBean("Govt Sales"));
				rightList.add(new CommonNameBean("Private Sales"));
				Department department2 = (Department) commonService.getAnObjectByAnyUniqueColumn("Department",
						"dept_keyword", SALES);
				for (Employee employee : employeeList) {
					if (employee.getDepartment().getDeptKeyword() == null || employee.getSubDepartment() == null) {
						continue;
					} else {
						if (employee.getSubDepartment().getDeptKeyword().trim().equals(GOVT_SALES.trim())
								|| employee.getSubDepartment().getDeptKeyword().trim().equals(PRIVATE_SALES.trim())) {
							if (employee.getSubDepartment().getDeptKeyword().trim().equals(GOVT_SALES.trim())) {
								leftList.add(new CommonNameBean(
										employee.getDesignation().getName() + "(" + employee.getName() + ")"));
							}
							if (employee.getSubDepartment().getDeptKeyword().trim().equals(PRIVATE_SALES.trim())) {
								rightList.add(new CommonNameBean(
										employee.getDesignation().getName() + "(" + employee.getName() + ")"));
							}
						} else {
							continue;
						}
					}
				}
				// rightList.add(new
				// CommonNameBean(department2.getDeptHead().getDesignation().getName() + "("+
				// department2.getDeptHead().getName() + ")"));
				dupmap.put(department2.getDeptHead().getId(), department2.getDeptHead().getId());
			}
			if (department.getDeptKeyword().trim().equals(PROCUREMENT.trim())
					|| department.getDeptKeyword().trim().equals(OPERATION.trim())
					|| department.getDeptKeyword().trim().equals(ACCOUNTS.trim())) {
				List<Employee> empListUnderDead = (List<Employee>) (Object) commonService
						.getObjectListByAnyColumn("Employee", "manager_id", department.getDeptHead().getId().toString());
				List<Employee> empSortList = new ArrayList<>();
				for (Employee employee : empListUnderDead) {
					if (employee.getDepartment() != null && employee.getDepartment().getId() == department.getId()) {
						empSortList.add(employee);
					}
				}
				if(empSortList.size()>0)
				rightList.add(new CommonNameBean(
						empSortList.get(0).getDesignation().getName() + "(" + empSortList.get(0).getName() + ")"));
				if(empSortList.size()>1)
				leftList.add(new CommonNameBean(
						empSortList.get(1).getDesignation().getName() + "(" + empSortList.get(1).getName() + ")"));

				for (Employee employee : employeeList) {
					if (empSortList.size()>0&&empSortList.get(0).getId().toString().equals(employee.getManager().getId().toString())) {
						rightList.add(
								new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (empSortList.size()>1&&empSortList.get(1).getId().toString().equals(employee.getManager().getId().toString())) {
						leftList.add(
								new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}

				}
			}
			for (Employee employee : employeeList) {
				if (department.getDeptHead() != null
						&& department.getDeptHead().getId().toString().equals(employee.getId().toString())) {
					continue;
				} else {
					if (employee.getDepartment() != null && employee.getDepartment().getDeptKeyword().toString().trim()
							.equals(HR_ADMIN.toString().trim())) {
						if (employee.getSubDepartment() != null && employee.getSubDepartment().getDeptKeyword().toString()
								.trim().equals(HR.toString().trim())) {

							leftList.add(new CommonNameBean(
									employee.getDesignation().getName() + "(" + employee.getName() + ")"));

						}
						if (employee.getSubDepartment() != null && employee.getSubDepartment().getDeptKeyword().toString()
								.trim().equals(ADMIN.toString().trim())) {

							if (!dupmap.containsKey(employee.getId())) {
								rightList.add(new CommonNameBean(
										employee.getDesignation().getName() + "(" + employee.getName() + ")"));
							}

						}
					}

					// if(employee.getDepartment()!=null
					// &&employee.getDepartment().getDeptKeyword().toString().trim().equals(PROCUREMENT.trim()))
					// {
					//
					// }

					if (department.getDeptKeyword() != null
							&& (department.getDeptKeyword().trim().equals(IMPORT_EXPORT.trim())
									|| department.getDeptKeyword().trim().equals(STORE_LOGISTIC.trim()))
							|| department.getDeptKeyword().trim().equals(PRIVATE_SALES.trim())
							|| department.getDeptKeyword().trim().equals(GOVT_SALES.trim())) {
						if (employeeList.size() / 2 <= employeeList.indexOf(employee)) {
							leftList.add(new CommonNameBean(
									employee.getDesignation().getName() + "(" + employee.getName() + ")"));
						}
						if (employeeList.size() / 2 > employeeList.indexOf(employee)) {
							rightList.add(new CommonNameBean(
									employee.getDesignation().getName() + "(" + employee.getName() + ")"));
						}
					}

				}
			}
			reportDeptList.add(new ReportDeptWiseBean(department.getName(),
					department.getDeptHead().getDesignation().getName() + "(" + department.getDeptHead().getName() + ")"));

			JRDataSource JRdataSource = new JRBeanCollectionDataSource(reportDeptList);

			parameterMap.put("datasource", JRdataSource);
			parameterMap.put("SUBREPORT_DATA_LEFT", leftList);
			if (department.getDeptKeyword() != null && department.getDeptKeyword().trim().equals(CUSTOMER_SERVICE.trim())) {
				parameterMap.put("SUBREPORT_DATA_RIGHT_L", rightList_L);
				parameterMap.put("SUBREPORT_DATA_RIGHT_R", rightList_R);
				parameterMap.put("secondHand", empUnderServiceHead.get(1).getDesignation().getName() + "("
						+ empUnderServiceHead.get(1).getName() + ")");
				modelAndView = new ModelAndView("organogramDeptWiseSubDept", parameterMap);
			} else if (department.getBusinessType().toString().trim().equals(BUSINESS_TYPE.TRADE.toString().trim())&& department.getDeptKeyword() != null && (department.getDeptKeyword().trim().equals(TENDER.trim())
					|| department.getDeptKeyword().trim().equals(PRODUCT_SOURCING.trim()))) {
				List<Employee> employees = (List<Employee>) (Object) commonService.getObjectListByTwoColumn("Employee",
						"manager_id", department.getDeptHead().getId().toString(), "department_id",
						department.getId().toString());
				String underDepthead = "";
				for (Employee employee2 : employees) {
					if (employee2.getManager().getId().toString().equals(department.getDeptHead().getId().toString())) {
						underDepthead = employee2.getDesignation().getName() + "(" + employee2.getName() + ")";
						List<Employee> emplist=new ArrayList<>();
						for (Employee employee3 : employeeList) {
							if(employee2.getId()!=employee3.getId()) {
								emplist.add(employee3);
							}
						}
						for (Employee employee23 : emplist) {
							if(employee23.getManager().getId()==employee2.getId()) {
								if (emplist.size() / 2 <= emplist.indexOf(employee23)) {
									leftList.add(new CommonNameBean(
											employee23.getDesignation().getName() + "(" + employee23.getName() + ")"));
								}
								if (emplist.size() / 2 > emplist.indexOf(employee23)) {
									rightList.add(new CommonNameBean(
											employee23.getDesignation().getName() + "(" + employee23.getName() + ")"));
								}
							}
							
						}
						break;
					}
				}
				parameterMap.put("underDepHead",underDepthead);
				parameterMap.put("SUBREPORT_DATA_LEFT", leftList);
				parameterMap.put("SUBREPORT_DATA_RIGHT", rightList);
				modelAndView = new ModelAndView("organogramDeptWiseTenderPre", parameterMap);
			}

			else {
				parameterMap.put("SUBREPORT_DATA_RIGHT", rightList);
				modelAndView = new ModelAndView("deptWiseReportPdf", parameterMap);
			}
		}
		
		else {
			
			reportDeptList.add(new ReportDeptWiseBean(department.getName(),
					department.getDeptHead().getDesignation().getName() + "(" + department.getDeptHead().getName() + ")"));

			JRDataSource JRdataSource = new JRBeanCollectionDataSource(reportDeptList);
			parameterMap.put("datasource", JRdataSource);
			parameterMap.put("underDepHead", department.getDeptHead().getDesignation().getName() + "(" + department.getDeptHead().getName() + ")");
			for (Employee employee : employeeList) {
				if (employeeList.size() / 2 <= employeeList.indexOf(employee)) {
					leftList.add(new CommonNameBean(
							employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (employeeList.size() / 2 > employeeList.indexOf(employee)) {
					rightList.add(new CommonNameBean(
							employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
			}
			parameterMap.put("SUBREPORT_DATA_LEFT", leftList);
			parameterMap.put("SUBREPORT_DATA_RIGHT", rightList);
			modelAndView = new ModelAndView("deptWiseReportPdf", parameterMap);
		}
			
		if(department.getDeptKeyword()!=null &&  department.getDeptKeyword().trim().equals(SALES.trim())|| department.getDeptKeyword().trim().equals(GOVT_SALES.trim())||department.getDeptKeyword().trim().equals(PRIVATE_SALES.trim())) {
			modelAndView = new ModelAndView("organogramDeptWiseSales", parameterMap);
		}
		
		if(department.getDeptKeyword()!=null && department.getDeptKeyword().trim().equals(SALES.trim())) {
			modelAndView = new ModelAndView("organogramDeptWiseSalesAll", parameterMap);
		}

		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	// @RequestMapping(value = "/downLoadDeptWise", method = RequestMethod.GET)
	@ResponseBody
	public void downLoadDeptWise(@ModelAttribute("command") Department command, HttpServletRequest request,
			HttpServletResponse response) throws JRException, IOException {
		List<CommonNameBean> leftList = new ArrayList<>();
		List<CommonNameBean> rightList = new ArrayList<>();
		List<ReportDeptWiseBean> reportDeptList = new ArrayList<>();
		Department department = (Department) commonService.getAnObjectByAnyUniqueColumn("Department", "id",
				request.getParameter("id"));
		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee",
				"department_id", request.getParameter("id"));
		Map<Integer, Integer> dupmap = new HashMap<>();
		if (department.getDeptKeyword().toString().trim().equals(HR_ADMIN.toString().trim())) {
			leftList.add(new CommonNameBean("Human Resource"));
			rightList.add(new CommonNameBean("Admin"));
			Department department2 = (Department) commonService.getAnObjectByAnyUniqueColumn("Department",
					"dept_keyword", "ADMIN");
			rightList.add(new CommonNameBean(department2.getDeptHead().getDesignation().getName() + "("
					+ department2.getDeptHead().getName() + ")"));
			dupmap.put(department2.getDeptHead().getId(), department2.getDeptHead().getId());
		}
		for (Employee employee : employeeList) {
			if (employee.getDepartment().getDeptKeyword().toString().trim().equals(HR_ADMIN.toString().trim())) {
				if (employee.getSubDepartment().getDeptKeyword().toString().trim().equals(HR.toString().trim())) {

					leftList.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));

				}
				if (employee.getSubDepartment().getDeptKeyword().toString().trim().equals(ADMIN.toString().trim())) {

					if (!dupmap.containsKey(employee.getId())) {
						rightList.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}

				}
			} else {
				if (employeeList.size() / 2 <= employeeList.indexOf(employee)) {
					leftList.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (employeeList.size() / 2 > employeeList.indexOf(employee)) {
					rightList.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
			}

		}

		reportDeptList.add(new ReportDeptWiseBean(department.getName(), department.getDeptHead().getName()));
		JRDataSource jRdataSource = null;
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/report/organogramDeptWise.jasper");
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> datasourcemap = new HashMap<>();
		ReportDeptWiseBean reportDeptWiseBean = new ReportDeptWiseBean();
		datasourcemap.put("reportDeptWiseBean", reportDeptWiseBean);
		jRdataSource = new JRBeanCollectionDataSource(reportDeptList, false);
		params.put("datasource", jRdataSource);
		params.put("SUBREPORT_DATA_LEFT", leftList);
		params.put("SUBREPORT_DATA_RIGHT", rightList);

		// prepare report first for one
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRdataSource);

		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition", "inline; filename=reportDeptWise" + department.getName() + ".pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/downLoadTeamWise")
	public ModelAndView generateTeamLeaderWiseReportPdf(ModelAndView modelAndView,
			@ModelAttribute("command") Department command, HttpServletRequest request, HttpServletResponse response)
			throws JRException, IOException {
		logger.debug("--------------generate PDF report----------");
		Map<String, Object> parameterMap = new HashMap<String, Object>();

		List<CommonNameBean> reportOne = new ArrayList<>();
		List<CommonNameBean> reportTwo = new ArrayList<>();
		List<CommonNameBean> reportThree = new ArrayList<>();
		List<CommonNameBean> reportFour = new ArrayList<>();
		List<CommonNameBean> reportFive = new ArrayList<>();
		List<CommonNameBean> reportSix = new ArrayList<>();
		List<CommonNameBean> reportSeven = new ArrayList<>();
		List<CommonNameBean> reportEight = new ArrayList<>();
		List<CommonNameBean> reportNine = new ArrayList<>();
		List<CommonNameBean> reportTen = new ArrayList<>();
		List<CommonNameBean> reportEleven = new ArrayList<>();
		List<CommonNameBean> reportTwelve = new ArrayList<>();
		String pointOne = "", pointTwo = "", pointThree = "", pointFour = "", pointFive = "", pointSix = "",
				pointSeven = "", pointEight = "", pointNine = "", pointTen = "", pointEleven = "", pointTwelve = "";
		List<ReportDeptWiseBean> reportDeptList = new ArrayList<>();
		Employee employeeBn = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				request.getParameter("id"));
		List<Point> points = (List<Point>) (Object) commonService.getObjectListByAnyColumn("Point", "team_leader_id",
				request.getParameter("id"));
		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee",
				"status", "1");
		Map<Integer, Integer> dupmap = new HashMap<>();
		String pointNameList = "";
		for (Point point : points) {
			pointNameList = pointNameList + point.getPointKeyword() + ", ";

			if (employeeBn.getEmpId().trim().equals("S006")) {
				if (points.indexOf(point) == 0) {
					pointOne = point.getName();
				}
			} else {
				if (points.indexOf(point) == 0) {
					pointOne = point.getName();
				}
				if (points.indexOf(point) == 1) {
					pointTwo = point.getName();
				}
				if (points.indexOf(point) == 2) {
					pointThree = point.getName();
				}
				if (points.indexOf(point) == 3) {
					pointFour = point.getName();
				}
				if (points.indexOf(point) == 4) {
					pointFive = point.getName();
				}
				if (points.indexOf(point) == 5) {
					pointSix = point.getName();
				}
				if (points.indexOf(point) == 6) {
					pointSeven = point.getName();
				}
				if (points.indexOf(point) == 7) {
					pointEight = point.getName();
				}
				if (points.indexOf(point) == 8) {
					pointNine = point.getName();
				}
				if (points.indexOf(point) == 9) {
					pointTen = point.getName();
				}
				if (points.indexOf(point) == 10) {
					pointEleven = point.getName();
				}
				if (points.indexOf(point) == 11) {
					pointTwelve = point.getName();
				}
			}

			for (Employee employee : employeeList) {
				if (employeeBn.getEmpId().trim().equals("S006")) {
					if (points.indexOf(point) == 0 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportOne.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
				} else {
					if (points.indexOf(point) == 0 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportOne.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 1 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportTwo.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 2 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportThree.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 3 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportFour.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 4 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportFive.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 5 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportSix.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 6 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportSeven.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 7 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportEight.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 8 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportNine.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 9 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportTen.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 10 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportEleven.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
					if (points.indexOf(point) == 11 && employee.getPoint() != null
							&& employee.getPoint().getId() == point.getId()) {

						reportTwelve.add(new CommonNameBean(
								employee.getDesignation().getName() + "(" + employee.getName() + ")"));
					}
				}

			}

		}

		// remove last char
		if (pointNameList.trim().length() > 0) {
			pointNameList = pointNameList.substring(0, pointNameList.length() - 2);
			pointNameList += " Point";
		}
		String xmlViewName = "";
		reportDeptList.add(new ReportDeptWiseBean(employeeBn.getName(), employeeBn.getDepartment().getName()));

		JRDataSource JRdataSource = new JRBeanCollectionDataSource(reportDeptList);

		Map<String, Object> datasourcemap = new HashMap<>();
		ReportDeptWiseBean reportDeptWiseBean = new ReportDeptWiseBean();
		datasourcemap.put("reportDeptWiseBean", reportDeptWiseBean);

		parameterMap.put("datasource", JRdataSource);

		if (employeeBn.getEmpId().trim().equals("S006")) {
			parameterMap.put("SUBREPORT_DATA_ONE", reportOne);
			parameterMap.put("pointOne", pointOne);
		} else {
			parameterMap.put("SUBREPORT_DATA_ONE", reportOne);
			parameterMap.put("SUBREPORT_DATA_TWO", reportTwo);
			parameterMap.put("SUBREPORT_DATA_THREE", reportThree);
			parameterMap.put("SUBREPORT_DATA_FOUR", reportFour);
			parameterMap.put("SUBREPORT_DATA_FIVE", reportFive);
			parameterMap.put("SUBREPORT_DATA_SIX", reportSix);
			parameterMap.put("SUBREPORT_DATA_SEVEN", reportSeven);
			parameterMap.put("SUBREPORT_DATA_EIGHT", reportEight);
			parameterMap.put("SUBREPORT_DATA_NINE", reportNine);
			parameterMap.put("SUBREPORT_DATA_TEN", reportTen);
			parameterMap.put("SUBREPORT_DATA_ELEVEN", reportEleven);
			parameterMap.put("SUBREPORT_DATA_TWELVE", reportTwelve);
			parameterMap.put("pointOne", pointOne);
			parameterMap.put("pointTwo", pointTwo);
			parameterMap.put("pointThree", pointThree);
			parameterMap.put("pointFour", pointFour);
			parameterMap.put("pointFive", pointFive);
			parameterMap.put("pointSix", pointSix);
			parameterMap.put("pointSeven", pointSeven);
			parameterMap.put("pointEight", pointEight);
			parameterMap.put("pointNine", pointNine);
			parameterMap.put("pointTen", pointTen);
			parameterMap.put("pointEleven", pointEleven);
			parameterMap.put("pointTwelve", pointTwelve);
		}
		if (employeeBn.getEmpId().trim().equals("S009")) {
			xmlViewName = "organoGramTMLDWtowfiq";
		} else if (employeeBn.getEmpId().trim().equals("S015")) {
			xmlViewName = "organoGramTMLDWMahbub";
		} else if (employeeBn.getEmpId().trim().equals("S006")) {
			xmlViewName = "organoGramTMLDWAHMTA";
		}

		else {
			xmlViewName = "teamLeaderWiseReportPdf";
		}
		if (employeeBn.getEmpId().trim().equals("S006")) {
			parameterMap.put("teamLeaderName",
					employeeBn.getDesignation().getName() + "(" + employeeBn.getName() + ")");
			parameterMap.put("titleRepo",
					"Organogram-" + employeeBn.getDepartment().getName() + "-" + employeeBn.getName());
		} else {
			parameterMap.put("teamLeaderName",
					employeeBn.getDesignation().getName() + "(" + employeeBn.getName() + ")");
			parameterMap.put("titleRepo",
					"Organogram-" + employeeBn.getDepartment().getName() + "-" + employeeBn.getName());
			parameterMap.put("pointlist", pointNameList);
		}

		modelAndView = new ModelAndView(xmlViewName, parameterMap);
		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	// @RequestMapping(value = "/downLoadTeamWise", method = RequestMethod.GET)
	@ResponseBody
	public void downLoadTeamWise(@ModelAttribute("command") Department command, HttpServletRequest request,
			HttpServletResponse response) throws JRException, IOException {
		List<CommonNameBean> reportOne = new ArrayList<>();
		List<CommonNameBean> reportTwo = new ArrayList<>();
		List<CommonNameBean> reportThree = new ArrayList<>();
		List<CommonNameBean> reportFour = new ArrayList<>();
		List<CommonNameBean> reportFive = new ArrayList<>();
		List<CommonNameBean> reportSix = new ArrayList<>();
		List<CommonNameBean> reportSeven = new ArrayList<>();
		List<CommonNameBean> reportEight = new ArrayList<>();
		List<CommonNameBean> reportNine = new ArrayList<>();
		List<CommonNameBean> reportTen = new ArrayList<>();
		List<CommonNameBean> reportEleven = new ArrayList<>();
		List<CommonNameBean> reportTwelve = new ArrayList<>();
		String pointOne = "", pointTwo = "", pointThree = "", pointFour = "", pointFive = "", pointSix = "",
				pointSeven = "", pointEight = "", pointNine = "", pointTen = "", pointEleven = "", pointTwelve = "";
		List<ReportDeptWiseBean> reportDeptList = new ArrayList<>();
		Employee employeeBn = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				request.getParameter("id"));
		List<Point> points = (List<Point>) (Object) commonService.getObjectListByAnyColumn("Point", "team_leader_id",
				request.getParameter("id"));
		List<Employee> employeeList = (List<Employee>) (Object) commonService.getAllObjectList("Employee");
		Map<Integer, Integer> dupmap = new HashMap<>();
		String pointNameList = "";
		for (Point point : points) {
			pointNameList = pointNameList + point.getName() + ",";
			if (points.indexOf(point) == 0) {
				pointOne = point.getName();
			}
			if (points.indexOf(point) == 1) {
				pointTwo = point.getName();
			}
			if (points.indexOf(point) == 2) {
				pointThree = point.getName();
			}
			if (points.indexOf(point) == 3) {
				pointFour = point.getName();
			}
			if (points.indexOf(point) == 4) {
				pointFive = point.getName();
			}
			if (points.indexOf(point) == 5) {
				pointSix = point.getName();
			}
			if (points.indexOf(point) == 6) {
				pointSeven = point.getName();
			}
			if (points.indexOf(point) == 7) {
				pointEight = point.getName();
			}
			if (points.indexOf(point) == 8) {
				pointNine = point.getName();
			}
			if (points.indexOf(point) == 9) {
				pointTen = point.getName();
			}
			if (points.indexOf(point) == 10) {
				pointEleven = point.getName();
			}
			if (points.indexOf(point) == 11) {
				pointTwelve = point.getName();
			}

			for (Employee employee : employeeList) {
				if (points.indexOf(point) == 0 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportOne.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 1 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportTwo.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 2 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportThree.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 3 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportFour.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 4 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportFive.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 5 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportSix.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 6 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportSeven.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 7 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportEight.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 8 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportNine.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 9 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportTen.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 10 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportEleven.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}
				if (points.indexOf(point) == 11 && employee.getPoint() != null
						&& employee.getPoint().getId() == point.getId()) {

					reportTwelve.add(
							new CommonNameBean(employee.getDesignation().getName() + "(" + employee.getName() + ")"));
				}

			}

		}

		reportDeptList.add(new ReportDeptWiseBean(employeeBn.getName(), employeeBn.getDepartment().getName()));
		JRDataSource jRdataSource = null;
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/report/organoGramTMLDW.jasper");
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> datasourcemap = new HashMap<>();
		ReportDeptWiseBean reportDeptWiseBean = new ReportDeptWiseBean();
		datasourcemap.put("reportDeptWiseBean", reportDeptWiseBean);
		jRdataSource = new JRBeanCollectionDataSource(reportDeptList, false);
		params.put("datasource", jRdataSource);
		params.put("SUBREPORT_DATA_ONE", reportOne);
		params.put("SUBREPORT_DATA_TWO", reportTwo);
		params.put("SUBREPORT_DATA_THREE", reportThree);
		params.put("SUBREPORT_DATA_FOUR", reportFour);
		params.put("SUBREPORT_DATA_FIVE", reportFive);
		params.put("SUBREPORT_DATA_SIX", reportSix);
		params.put("SUBREPORT_DATA_SEVEN", reportSeven);
		params.put("SUBREPORT_DATA_EIGHT", reportEight);
		params.put("SUBREPORT_DATA_NINE", reportNine);
		params.put("SUBREPORT_DATA_TEN", reportTen);
		params.put("SUBREPORT_DATA_ELEVEN", reportEleven);
		params.put("SUBREPORT_DATA_TWELVE", reportTwelve);
		params.put("pointOne", pointOne);
		params.put("pointTwo", pointTwo);
		params.put("pointThree", pointThree);
		params.put("pointFour", pointFour);
		params.put("pointFive", pointFive);
		params.put("pointSix", pointSix);
		params.put("pointSeven", pointSeven);
		params.put("pointEight", pointEight);
		params.put("pointNine", pointNine);
		params.put("pointTen", pointTen);
		params.put("pointEleven", pointEleven);
		params.put("pointTwelve", pointTwelve);
		params.put("teamLeaderName", employeeBn.getDesignation().getName() + "(" + employeeBn.getName() + ")");
		params.put("titleRepo", "Organogram-" + employeeBn.getDepartment().getName() + "-" + employeeBn.getName());
		params.put("pointlist", pointNameList);

		// prepare report first for one
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRdataSource);

		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition",
				"inline; filename=reportOrganogramTemWise" + employeeBn.getName() + ".pdf");
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reportFormDeptWise", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView reportFormDeptWise(@ModelAttribute("command") Department command, ModelMap model,
			HttpServletRequest request, HttpServletResponse response, Principal principal) {
		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		List<Department> deptList = (List<Department>) (Object) commonService.getAllObjectList("Department");
		model.put("deptList", deptList);
		return new ModelAndView("reportFormDeptWise", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reportFormTeamWise", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView reportFormTeamWise(@ModelAttribute("command") Employee command, ModelMap model,
			HttpServletRequest request, HttpServletResponse response, Principal principal) {
		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		List<Employee> empList = (List<Employee>) (Object) commonService.getAllObjectList("Employee");

		List<Point> pointList = (List<Point>) (Object) commonService.getAllObjectList("Point");
		List<Employee> employees = new ArrayList<>();
		Map<Integer, Integer> dupMap = new HashMap<>();
		for (Point point : pointList) {
			for (Employee employee : empList) {
				if (point.getTeamLeader() != null && point.getTeamLeader().getId().equals(employee.getId())) {
					if (!dupMap.containsKey(employee.getId())) {
						employees.add(employee);
						dupMap.put(employee.getId(), employee.getId());
					}

					break;
				}
			}
		}
		model.put("empList", employees);
		return new ModelAndView("reportFormTeamWise", model);
	}

}

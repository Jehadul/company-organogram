package com.nazdaq.organogram;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.organogram.model.Department;
import com.nazdaq.organogram.model.Employee;
import com.nazdaq.organogram.service.CommonService;
import com.nazdaq.organogram.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class DepartmentController  implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addDepartmentForm", method = RequestMethod.GET)
	public ModelAndView departmentForm (@ModelAttribute("departmentForm") Department departmentBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Department> departmentList = (List<Department>)(Object)
				commonService.getAllObjectList("Department");	
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByAnyColumn("Employee", "status", ACTIVE);
		
		List<String> enumNames = Stream.of(BUSINESS_TYPE.values())
                .map(BUSINESS_TYPE::name)
                .collect(Collectors.toList());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("departmentList", departmentList);		
		model.put("employeeList", employeeList);
		model.put("BUSINESS_TYPE", enumNames);
		return new ModelAndView("addDepartmentForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editDepartment", method = RequestMethod.GET)
	public ModelAndView editDepartment (@ModelAttribute("departmentForm") Department departmentBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Department> departmentList = (List<Department>)(Object)
				commonService.getAllObjectList("Department");	
		Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "id", departmentBean.getId().toString());
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByAnyColumn("Employee", "status", ACTIVE);
		
		List<String> enumNames = Stream.of(BUSINESS_TYPE.values())
                .map(BUSINESS_TYPE::name)
                .collect(Collectors.toList());
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("departmentList", departmentList);
		model.put("employeeList", employeeList);
		model.put("edit", true);
		model.put("BUSINESS_TYPE", enumNames);
		model.put("department", department);
		return new ModelAndView("addDepartmentForm", model);		
	}
	
	@RequestMapping(value = "/saveDepartment", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDepartment (@ModelAttribute("departmentForm") Department departmentBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		Employee deptHead = null;
		if(departmentBean.getDeptHeadId() != null) {
			deptHead = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", departmentBean.getDeptHeadId().toString());
		}
		
		
		if(departmentBean.getId() != null) {
			Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "id", departmentBean.getId().toString());
			List<Department> deptList = (List<Department>)(Object)commonService.getObjectListByTwoColumn("Department", "businessType", departmentBean.getBusinessType(), 
					"deptKeyword", departmentBean.getDeptKeyword());
			//Department departmentbyName = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "deptKeyword", departmentBean.getDeptKeyword().toString());
			if(deptList != null && deptList.size() > 0) {
				if(!deptList.get(0).getId().toString().equals(department.getId().toString())) {
					model.put("message", "addDepartmentForm");
					return new ModelAndView("failed", model);
				} else {
					department.setName(departmentBean.getName());
					department.setDeptHead(deptHead);
					department.setRemarks(departmentBean.getRemarks());
					department.setModifiedBy(principal.getName());
					department.setModifiedDate(new Date());
					department.setBusinessType(departmentBean.getBusinessType());
					commonService.saveOrUpdateModelObjectToDB(department);
				}
				
			}else {
				department.setName(departmentBean.getName());
				department.setDeptHead(deptHead);
				department.setRemarks(departmentBean.getRemarks());
				department.setModifiedBy(principal.getName());
				department.setModifiedDate(new Date());
				department.setBusinessType(departmentBean.getBusinessType());
				commonService.saveOrUpdateModelObjectToDB(department);
			}
		} else {
			//Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "deptKeyword", departmentBean.getDeptKeyword().toString());
			List<Department> deptList = (List<Department>)(Object)commonService.getObjectListByTwoColumn("Department", "businessType", departmentBean.getBusinessType(), 
					"deptKeyword", departmentBean.getDeptKeyword());
			if(deptList != null && deptList.size() > 0) {
				model.put("message", "addDepartmentForm");
				return new ModelAndView("failed", model);
			} else {
				departmentBean.setDeptHead(deptHead);
				departmentBean.setCreatedBy(principal.getName());
				departmentBean.setCreatedDate(new Date());
				departmentBean.setDeptKeyword(departmentBean.getDeptKeyword().toUpperCase());
				commonService.saveOrUpdateModelObjectToDB(departmentBean);
			}
		}
		
		
		model.put("message", "addDepartmentForm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteDepartment", method = RequestMethod.GET)
	public ModelAndView deleteDepartment (Department departmentBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Department", departmentBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "addDepartmentForm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkDepartment", method = RequestMethod.POST)
	 public @ResponseBody String checkDepartmentExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("deptKeyword") != null){
			//Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "deptKeyword", request.getParameter("deptKeyword").toString());
			List<Department> deptList = (List<Department>)(Object)commonService.getObjectListByTwoColumn("Department", "businessType", request.getParameter("businessType"), 
					"deptKeyword", request.getParameter("deptKeyword"));
			if(deptList != null && deptList.size() > 0) {
				if(request.getParameter("id").equals(deptList.get(0).getId().toString())){
					text = "true";
				} else {
					text = "false";
				}
			}			
		}	 
		return text;
	 }
	
	private String getReferenceNo(String referNo){
		String refNo = "001";
		if(referNo != null){
			Integer ref = 1;
			
			if(referNo != null) {
				 ref = Integer.parseInt(referNo)+1;
			} 
			
			if(ref.toString().length() == 1) {
				refNo = "00"+ref;
			} else if (ref.toString().length() == 2) {
				refNo = "0"+ref;
			} else {
				refNo = ref.toString();
			}
		}
		return refNo;
	}
	
	private String getFormatedMobileNo(String mobile){
		String mobileNo = "";
		String [] mobileNos = mobile.split(",");
		mobileNo = mobileNos[0];
		
		if(mobileNo.length() == 10){
			return "+880" + mobileNo;
		} else if(mobileNo.length() == 11){
			return "+88" + mobileNo;
		}else if(mobileNo.length() == 13){
			return "+" + mobileNo;
		} else if(mobileNo.length() == 14){
			return mobileNo;
		} else {
			return mobileNo = "";
		}
	}
}

package com.nazdaq.organogram;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.organogram.model.Department;
import com.nazdaq.organogram.model.Designation;
import com.nazdaq.organogram.model.Employee;
import com.nazdaq.organogram.model.Point;
import com.nazdaq.organogram.service.CommonService;
import com.nazdaq.organogram.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class EmployeeController implements Constants {

	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addEmployeeForm", method = RequestMethod.GET)
	public ModelAndView addEmployeeForm(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		List<Department> departmentList = (List<Department>) (Object) commonService.getAllObjectList("Department");

		List<Designation> designationList = (List<Designation>) (Object) commonService.getAllObjectList("Designation");

		List<Point> pointList = (List<Point>) (Object) commonService.getAllObjectList("Point");

		List<Employee> managerList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee",
				"status", ACTIVE);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("designationList", designationList);
		model.put("pointList", pointList);
		model.put("departmentList", departmentList);
		model.put("managerList", managerList);
		return new ModelAndView("addEmployeeForm", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editEmployee", method = RequestMethod.GET)
	public ModelAndView editEmployee(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		List<Department> departmentList = (List<Department>) (Object) commonService.getAllObjectList("Department");

		List<Designation> designationList = (List<Designation>) (Object) commonService.getAllObjectList("Designation");

		List<Point> pointList = (List<Point>) (Object) commonService.getAllObjectList("Point");

		List<Employee> managerList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee",
				"status", ACTIVE);

		List<Employee> manList = new ArrayList<Employee>();
		Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
				employeeBean.getId().toString());
		for (Employee emp : managerList) {
			if (employee.getId().toString().equals(emp.getId().toString())) {
				continue;
			} else {
				manList.add(emp);
			}
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("designationList", designationList);
		model.put("pointList", pointList);
		model.put("departmentList", departmentList);
		model.put("managerList", manList);

		model.put("edit", true);
		model.put("employee", employee);

		return new ModelAndView("addEmployeeForm", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public ModelAndView saveEmployee(@ModelAttribute("employeeForm") Employee employeeBean, Principal principal
			) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		boolean flag = true;
		Employee manager = null;
		Department department = null; 
		Department subDepartment = null; 
		Designation designation = null;
		Point point = null;
		if (employeeBean.getManagerId() != null) {
			manager = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
					employeeBean.getManagerId().toString());
		}

		if (employeeBean.getPointId() != null) {
			point = (Point) commonService.getAnObjectByAnyUniqueColumn("Point", "id",
					employeeBean.getPointId().toString());
		}

		if (employeeBean.getDepartmentId() != null) {
			department = (Department) commonService.getAnObjectByAnyUniqueColumn("Department", "id",
					employeeBean.getDepartmentId().toString());
		}
		if (employeeBean.getSubDepartmentId() != null) {
			subDepartment = (Department) commonService.getAnObjectByAnyUniqueColumn("Department", "id",
					employeeBean.getSubDepartmentId().toString());
		}
		if (employeeBean.getDesignationId() != null) {
			designation = (Designation) commonService.getAnObjectByAnyUniqueColumn("Designation", "id",
					employeeBean.getDesignationId().toString());
		}
		Map<String, Object> model = new HashMap<String, Object>();

		if (employeeBean.getId() != null) {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id",
					employeeBean.getId().toString());
			Employee employeeByEmpId = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "empId",
					employeeBean.getEmpId());
			if (employeeByEmpId != null) {
				if (employee.getId().toString().equals(employeeByEmpId.getId().toString())) {
					employee.setModifiedBy(principal.getName());
					employee.setModifiedDate(new Date());
					//if (department != null)
					employee.setDepartment(department);
					//if (designation != null)
					employee.setDesignation(designation);
					//if (subDepartment != null)
					employee.setSubDepartment(subDepartment);
					employee.setManager(manager);
					employee.setPoint(point);

					employee.setStatus(employeeBean.getStatus());
					employee.setSortOrder(employeeBean.getSortOrder());
					employee.setRemarks(employeeBean.getRemarks());
					employee.setName(employeeBean.getName());
					commonService.saveOrUpdateModelObjectToDB(employee);
					flag = true;
				} else {
					flag = false;
				}
			} else {
				employee.setModifiedBy(principal.getName());
				employee.setModifiedDate(new Date());
				//if (department != null)
				employee.setDepartment(department);
				//if (designation != null)
				employee.setDesignation(designation);
				//if (subDepartment != null)
				employee.setSubDepartment(subDepartment);
				
				employee.setManager(manager);
				employee.setPoint(point);
			

				employee.setStatus(employeeBean.getStatus());
				employee.setSortOrder(employeeBean.getSortOrder());
				employee.setRemarks(employeeBean.getRemarks());
				employee.setName(employeeBean.getName());
				commonService.saveOrUpdateModelObjectToDB(employee);
				flag = true;
			}

		} else {
			Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "empId",
					employeeBean.getEmpId());
			if (employee == null) {
				employeeBean.setCreatedBy(principal.getName());
				employeeBean.setCreatedDate(new Date());
				//if (department != null)
				employeeBean.setDepartment(department);
				//if (designation != null)
				employeeBean.setDesignation(designation);
				//if (subDepartment != null)
				employeeBean.setSubDepartment(subDepartment);
				employeeBean.setManager(manager);
				employeeBean.setPoint(point);
				commonService.saveOrUpdateModelObjectToDB(employeeBean);
				flag = true;
			} else {
				flag = false;
			}
		}

		if (flag) {
			model.put("message", "activeEmployeeList");
			return new ModelAndView("success", model);
		} else {
			model.put("message", "addEmployeeForm");
			return new ModelAndView("failed", model);
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/activeEmployeeList", method = RequestMethod.GET)
	public ModelAndView activeEmployeeList(Employee employeeBean, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee",
				"status", ACTIVE);

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("employeeList", employeeList);
		model.put("status", "Active");
		return new ModelAndView("activeEmployeeList", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inactiveEmployeeList", method = RequestMethod.GET)
	public ModelAndView inactiveEmployeeList(Employee employeeBean, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		List<Employee> employeeList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee",
				"status", INACTIVE);

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("employeeList", employeeList);
		model.put("status", "InActive");
		return new ModelAndView("inactiveEmployeeList", model);
	}

	private String getReferenceNo(String referNo) {
		String refNo = "001";
		if (referNo != null) {
			Integer ref = 1;

			if (referNo != null) {
				ref = Integer.parseInt(referNo) + 1;
			}

			if (ref.toString().length() == 1) {
				refNo = "00" + ref;
			} else if (ref.toString().length() == 2) {
				refNo = "0" + ref;
			} else {
				refNo = ref.toString();
			}
		}
		return refNo;
	}

	private String getFormatedMobileNo(String mobile) {
		String mobileNo = "";
		String[] mobileNos = mobile.split(",");
		mobileNo = mobileNos[0];

		if (mobileNo.length() == 10) {
			return "+880" + mobileNo;
		} else if (mobileNo.length() == 11) {
			return "+88" + mobileNo;
		} else if (mobileNo.length() == 13) {
			return "+" + mobileNo;
		} else if (mobileNo.length() == 14) {
			return mobileNo;
		} else {
			return mobileNo = "";
		}
	}
}

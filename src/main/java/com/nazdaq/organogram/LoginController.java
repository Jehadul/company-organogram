package com.nazdaq.organogram;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.organogram.bean.DashboardBean;
import com.nazdaq.organogram.model.Department;
import com.nazdaq.organogram.model.Employee;
import com.nazdaq.organogram.model.User;
import com.nazdaq.organogram.service.CommonService;
import com.nazdaq.organogram.service.UserService;
import com.nazdaq.organogram.util.Constants;



@Controller
public class LoginController extends SavedRequestAwareAuthenticationSuccessHandler implements Constants{
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonService commonService;
	
	
	@RequestMapping(value="/success", method = RequestMethod.GET)
	public String success(ModelMap model) {
	return "success"; 
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/","/index"}, method = RequestMethod.GET)
	public ModelAndView printWelcome1(ModelMap model, Principal principal, HttpSession session, HttpServletRequest request) throws ParseException {
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		/*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
		String roleName = "";
		for (GrantedAuthority ga : auth) {
			roleName = ga.getAuthority();
			break;
		}*/
	
		
		String roleName = 	commonService.getAuthRoleName();
		
		String pageLocation=null;
		User user=null;
		String name = principal.getName();
		user=userService.getUser(name);			
		
		session.setAttribute("userr", name);
		session.setAttribute("uid", 1);
		session.setAttribute("userrId", session.getAttribute("userr"));
		session.setAttribute("roleName", roleName);
		model.addAttribute("userName", session.getAttribute("userr"));
		model.addAttribute("userId", session.getAttribute("userrId"));
		
		
		Map<String, Object> modelStr = new HashMap<String, Object>();
		List<Department> departmentList = (List<Department>)(Object) commonService.getAllObjectList("Department");		
		DashboardBean db = new DashboardBean();
		List<Employee>empList=(List<Employee>) (Object)commonService.getAllObjectList("Employee");
		if(request.isUserInRole(ROLE_ADMIN) || request.isUserInRole(ROLE_SUPER_ADMIN)){
			for (Department department : departmentList) {
				if(department.getDeptKeyword()!=null && department.getDeptHead() != null) {
				if(department.getDeptKeyword()!=null&&department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(SALES.toString().trim())) {
					db.setTradeSales(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim().trim()) && department.getDeptKeyword().equals(PROCUREMENT.toString().trim())) {
					db.setTradeProcurement(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(CUSTOMER_SERVICE.toString().trim())) {
					db.setTradeCustomerService(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(TENDER.toString().trim())) {
					db.setTradeTender(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(HR_ADMIN.toString().trim())) {
					db.setTradeHrAdmin(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(ASSETS.toString().trim())) {
					db.setTradeAssets(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(PRODUCT_SOURCING.toString().trim())) {
					db.setTradeProductSource(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(STORE_LOGISTIC.toString().trim())) {
					db.setTradeStoreLog(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(ACCOUNTS.toString().trim())) {
					db.setTradeAccounts(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(IMPORT_EXPORT.toString().trim())) {
					db.setTradeImportExport(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(VAT_TAX.toString().trim())) {
					db.setTradeVatTax(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(OPERATION.toString().trim())) {
					db.setTradeOperation(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(GOVT_SALES.toString().trim())) {
				//	List<Employee> empList = (List<Employee>) (Object) commonService.getObjectListByAnyColumn("Employee", "subDeptKeyword", GOVT_SALES);
					List<String> govtSales = new ArrayList<String>();
					for (Employee employee : empList) {
						if(employee.getSubDepartment() != null && employee.getSubDepartment().getDeptKeyword().equals(GOVT_SALES.toString().trim())) {
							govtSales.add(employee.getDesignation().getName() + "<br/>(" + employee.getName() + ")");
						}
						
					}
					db.setGovtSales(govtSales);
				}				
				if(department.getBusinessType().equals(BUSINESS_TYPE.TRADE.toString().trim()) && department.getDeptKeyword().equals(PRIVATE_SALES.toString().trim())) {
				
					List<String> privateSales = new ArrayList<String>();
					for (Employee employee : empList) {
						if(employee.getSubDepartment() != null &&employee.getSubDepartment().getDeptKeyword().equals(PRIVATE_SALES.toString().trim())) {
							privateSales.add(employee.getDesignation().getName() + "<br/>(" + employee.getName() + ")");
						}
						
					}
					db.setPrivateSales(privateSales);
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim().trim()) && department.getDeptKeyword().equals(ACCOUNTS.toString().trim())) {
					db.setEmerAccounts(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim()) && department.getDeptKeyword().equals(PROCUREMENT.toString().trim())) {
					db.setEmerProcurement(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim()) && department.getDeptKeyword().equals(PRODUCT_SOURCING.toString().trim())) {
					db.setEmerProductSource(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim()) && department.getDeptKeyword().equals(STORE_LOGISTIC.toString().trim())) {
					db.setEmerStoreLog(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim()) && department.getDeptKeyword().equals(SALES.toString().trim())) {
					db.setEmerSales(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				if(department.getBusinessType().equals(BUSINESS_TYPE.EMERGING.toString().trim()) && department.getDeptKeyword().equals(PROJECT_IMPL.toString().trim())) {
					db.setEmerProjectImpl(department.getDeptHead().getDesignation().getName() + "<br/>(" + department.getDeptHead().getName() + ")");
				}
				}
			}
			//return new ModelAndView("redirect:/pendCandidateList", modelStr);			
			//return new ModelAndView("redirect:/adminDashboard", modelStr);
			pageLocation="home";	
		} else {
			pageLocation="index";					
		}
		
		model.put("organogram", db);		
		return new ModelAndView(pageLocation, modelStr);
	}

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Principal principal) {
		if(principal != null) {
			return "redirect:/index";
		}
 		return "login";
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(Model model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(Model model, HttpSession session) {
		session.invalidate();
 		return "login";
 	}
	
	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	protected String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	private Integer getMonthKey(String month) {
		Map<String, Integer> months = new HashMap<String, Integer>();
		months.put("January", 1);
		months.put("February", 2);
		months.put("March", 3);
		months.put("April", 4);
		months.put("May", 5);
		months.put("June", 6);
		months.put("July", 7);
		months.put("August", 8);
		months.put("September", 9);
		months.put("October", 10);
		months.put("November", 11);
		months.put("December", 12);
		
		return months.get(month);
	}
	
	private String getMonthName(Integer key) {
		Map<Integer, String> months = new HashMap<Integer, String>();
		months.put(1, "January");
		months.put(2, "February");
		months.put(3, "March");
		months.put(4, "April");
		months.put(5, "May");
		months.put(6, "June");
		months.put(7, "July");
		months.put(8, "August");
		months.put(9, "September");
		months.put(10, "October");
		months.put(11, "November");
		months.put(12, "December");		
		return months.get(key);
	}
	
	
}

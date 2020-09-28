package com.nazdaq.organogram;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.nazdaq.organogram.model.Employee;
import com.nazdaq.organogram.model.Point;
import com.nazdaq.organogram.service.CommonService;
import com.nazdaq.organogram.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class PointController  implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addPointForm", method = RequestMethod.GET)
	public ModelAndView addPointForm (@ModelAttribute("pointForm") Point pointBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Point> pointList = (List<Point>)(Object)
				commonService.getAllObjectList("Point");		
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByAnyColumn("Employee", "status", ACTIVE);
		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("pointList", pointList);		
		model.put("employeeList", employeeList);
		return new ModelAndView("addPointForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editPoint", method = RequestMethod.GET)
	public ModelAndView editPoint (@ModelAttribute("pointForm") Point pointBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Point> pointList = (List<Point>)(Object)
				commonService.getAllObjectList("Point");	
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByAnyColumn("Employee", "status", ACTIVE);
		
		Point point = (Point)commonService.getAnObjectByAnyUniqueColumn("Point", "id", pointBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("pointList", pointList);
		model.put("employeeList", employeeList);
		model.put("edit", true);
		model.put("point", point);
		return new ModelAndView("addPointForm", model);		
	}
	
	@RequestMapping(value = "/savePoint", method = RequestMethod.POST)
	public ModelAndView saveOrUpdatePoint (@ModelAttribute("pointForm") Point pointBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		Employee teamLeader = null;
		if(pointBean.getTeamLeaderId() != null) {
			teamLeader = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "id", pointBean.getTeamLeaderId().toString());
		}
		
		
		if(pointBean.getId() != null) {
			Point point = (Point)commonService.getAnObjectByAnyUniqueColumn("Point", "id", pointBean.getId().toString());
			Point pointByKeyword = (Point)commonService.getAnObjectByAnyUniqueColumn("Point", "pointKeyword", pointBean.getPointKeyword().toString());
			if(pointByKeyword != null) {
				if(!pointByKeyword.getId().toString().equals(point.getId().toString())) {
					model.put("message", "addPointForm");
					return new ModelAndView("failed", model);
				} else {
					point.setName(pointBean.getName());
					point.setRemarks(pointBean.getRemarks());
					point.setTeamLeader(teamLeader);
					point.setPointKeyword(pointBean.getPointKeyword());
					point.setModifiedBy(principal.getName());
					point.setModifiedDate(new Date());
					commonService.saveOrUpdateModelObjectToDB(point);
				}
				
			}else {
				point.setName(pointBean.getName());
				point.setRemarks(pointBean.getRemarks());
				point.setTeamLeader(teamLeader);
				point.setPointKeyword(pointBean.getPointKeyword());
				point.setModifiedBy(principal.getName());
				point.setModifiedDate(new Date());
				commonService.saveOrUpdateModelObjectToDB(point);
			}
		} else {
			Point point = (Point)commonService.getAnObjectByAnyUniqueColumn("Point", "pointKeyword", pointBean.getPointKeyword().toString());
			if(point != null) {
				model.put("message", "addPointForm");
				return new ModelAndView("failed", model);
			} else {
				pointBean.setTeamLeader(teamLeader);
				pointBean.setCreatedBy(principal.getName());
				pointBean.setCreatedDate(new Date());
				pointBean.setPointKeyword(pointBean.getPointKeyword().toUpperCase());
				commonService.saveOrUpdateModelObjectToDB(pointBean);
			}
		}
		
		
		model.put("message", "addPointForm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deletePoint", method = RequestMethod.GET)
	public ModelAndView deletePoint (Point pointBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Point", pointBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "addPointForm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkPoint", method = RequestMethod.POST)
	 public @ResponseBody String checkPointExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("pointKeyword") != null){
			Point point = (Point)commonService.getAnObjectByAnyUniqueColumn("Point", "pointKeyword", request.getParameter("pointKeyword").toString());
			if(point != null){ 
				if(request.getParameter("id").equals(point.getId().toString())){
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

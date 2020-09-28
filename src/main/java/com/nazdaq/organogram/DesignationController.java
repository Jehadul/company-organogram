package com.nazdaq.organogram;

import java.security.Principal;
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

import com.nazdaq.organogram.model.Designation;
import com.nazdaq.organogram.service.CommonService;
import com.nazdaq.organogram.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class DesignationController  implements Constants{
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addDesignationForm", method = RequestMethod.GET)
	public ModelAndView designationForm (@ModelAttribute("designationForm") Designation designationBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Designation> designationList = (List<Designation>)(Object)
				commonService.getAllObjectList("Designation");		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("designationList", designationList);		
		return new ModelAndView("addDesignationForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editDesignation", method = RequestMethod.GET)
	public ModelAndView editDesignation (@ModelAttribute("designationForm") Designation designationBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Designation> designationList = (List<Designation>)(Object)
				commonService.getAllObjectList("Designation");	
		Designation designation = (Designation)commonService.getAnObjectByAnyUniqueColumn("Designation", "id", designationBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("designationList", designationList);
		model.put("edit", true);
		model.put("designation", designation);
		return new ModelAndView("addDesignationForm", model);		
	}
	
	@RequestMapping(value = "/saveDesignation", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDesignation (@ModelAttribute("designationForm") Designation designationBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(designationBean.getId() != null) {
			Designation designation = (Designation)commonService.getAnObjectByAnyUniqueColumn("Designation", "id", designationBean.getId().toString());
			Designation designationByName = (Designation)commonService.getAnObjectByAnyUniqueColumn("Designation", "name", designationBean.getName().toString());
			if(designationByName != null) {
				if(!designationByName.getId().toString().equals(designation.getId().toString())) {
					model.put("message", "addDesignationForm");
					return new ModelAndView("failed", model);
				} else {
					designation.setName(designationBean.getName());
					designation.setRemarks(designationBean.getRemarks());
					commonService.saveOrUpdateModelObjectToDB(designation);
				}
				
			}else {
				designation.setName(designationBean.getName());
				designation.setRemarks(designationBean.getRemarks());
				commonService.saveOrUpdateModelObjectToDB(designation);
			}
		} else {
			Designation designation = (Designation)commonService.getAnObjectByAnyUniqueColumn("Designation", "name", designationBean.getName().toString());
			if(designation != null) {
				model.put("message", "addDesignationForm");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(designationBean);
			}
		}
		
		
		model.put("message", "addDesignationForm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteDesignation", method = RequestMethod.GET)
	public ModelAndView deleteDesignation (Designation designationBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Designation", designationBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "addDesignationForm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkDesignation", method = RequestMethod.POST)
	 public @ResponseBody String checkDesignationExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("name") != null){
			Designation designation = (Designation)commonService.getAnObjectByAnyUniqueColumn("Designation", "name", request.getParameter("name").toString());
			if(designation != null){ 
				if(request.getParameter("id").equals(designation.getId().toString())){
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

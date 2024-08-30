package com.s13sh.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.s13sh.jobportal.dto.ApplicantDetails;
import com.s13sh.jobportal.service.ApplicantService;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/applicant")
@MultipartConfig
public class ApplicantController {

	@Autowired
	ApplicantService applicantService;
	
	@GetMapping("/home")
	public String loadHome() {
		return "applicant-home.html";
	}
	
	@GetMapping("/complete-profile")
	public String completeProfile() {
		return "applicant-profile.html";
	}

	@PostMapping("/complete-profile")
	public String completeProfile(ApplicantDetails details, @RequestParam MultipartFile resume, HttpSession session,
			ModelMap map) {
		return applicantService.completeProfile(details, resume, session, map);
	}
	
	@GetMapping("/view-jobs")
	public String viewJobs(HttpSession session,ModelMap map) {
		return applicantService.viewJobs(session,map);
	}
	
	@GetMapping("/apply-job/{id}")
	public String applyJob(HttpSession session,@PathVariable int id) {
		return applicantService.applyJob(id,session);
	}

}

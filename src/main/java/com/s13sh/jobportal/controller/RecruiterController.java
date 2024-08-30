package com.s13sh.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.s13sh.jobportal.dto.Job;
import com.s13sh.jobportal.dto.RecruiterDetails;
import com.s13sh.jobportal.service.RecruiterService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {

	@Autowired
	RecruiterService recruiterService;

	@GetMapping("/home")
	public String loadHome() {
		return "recruiter-home.html";
	}

	@GetMapping("/complete-profile")
	public String completeProfile(ModelMap map, HttpSession session) {
		return recruiterService.checkProfile(map, session);
	}

	@PostMapping("/complete-profile")
	public String completeProfile(RecruiterDetails details, HttpSession session, ModelMap map) {
		return recruiterService.saveRecruiterDetails(details, session, map);
	}

	@GetMapping("/post-job")
	public String loadPostJob(HttpSession session) {
		return recruiterService.postJob(session);
	}

	@PostMapping("/post-job")
	public String postJob(Job job, HttpSession session) {
		return recruiterService.postJob(job, session);
	}

	@GetMapping("/view-jobs")
	public String viewJobs(HttpSession session, ModelMap map) {
		return recruiterService.viewJobs(session, map);
	}
	
	@GetMapping("/view-applicants/{id}")
	public String viewApplicants(@PathVariable int id,HttpSession session,ModelMap map) {
		return recruiterService.viewApplicants(id,session,map);
	}

}

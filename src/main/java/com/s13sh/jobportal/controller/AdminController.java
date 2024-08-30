package com.s13sh.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.s13sh.jobportal.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;

	@GetMapping("/create-admin/{email}/{password}")
	public String createAdmin(HttpSession session,@PathVariable String email,@PathVariable String password) {
		return adminService.createAdmin(email,password,session);
	}
	
	@GetMapping("/home")
	public String loadHome() {
		return "admin-home.html";
	}
	
	@GetMapping("/fetch-recruiter")
	public String fetchRecruiter(HttpSession session,ModelMap map) {
		return adminService.fetchRecruiters(session,map);
	}
	
	@GetMapping("/complete-profile/{id}")
	public String completeProfile(@PathVariable int id,HttpSession session) {
		return adminService.completeProfile(id,session);
	}
	
	
	
}

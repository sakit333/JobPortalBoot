package com.s13sh.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.s13sh.jobportal.dto.PortalUser;
import com.s13sh.jobportal.service.PortalUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class GeneralController {

	@Autowired
	PortalUser portalUser;

	@Autowired
	PortalUserService userService;

	@GetMapping("/")
	public String loadHome() {
		return "home.html";
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "login.html";
	}

	@GetMapping("/signup")
	public String loadSignup(ModelMap map) {
		map.put("portalUser", portalUser);
		return "signup.html";
	}

	@PostMapping("/signup")
	public String signup(@Valid PortalUser portalUser, BindingResult result, HttpSession session) {
		return userService.signup(portalUser, result, session);
	}

	@GetMapping("/enter-otp")
	public String loadEnterOtp() {
		return "enter-otp.html";
	}

	@PostMapping("/submit-otp")
	public String submitOtp(@RequestParam int otp, @RequestParam int id, HttpSession session) {
		return userService.submitOtp(otp, id, session);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, HttpSession session) {
		return userService.resendOtp(id, session);
	}

	@PostMapping("/login")
	public String login(@RequestParam("email-phone") String emph, @RequestParam String password, ModelMap map,
			HttpSession session) {
		return userService.login(emph, password, map, session);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("portalUser");
		session.setAttribute("success", "Logout Success");
		return "redirect:/";
	}

}

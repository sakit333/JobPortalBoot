package com.s13sh.jobportal.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

	@RequestMapping("/error")
	public String myErrorHandler(HttpServletRequest request) {
		int code = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
		if (code == 404)
			return "404.html";
		else
			return "error.html";
	}
}

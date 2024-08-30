package com.s13sh.jobportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionDataRemover {

	public void removeMessage() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getSession();
		session.removeAttribute("failure");
		session.removeAttribute("success");
	}
}

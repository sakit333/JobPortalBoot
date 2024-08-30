package com.s13sh.jobportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.s13sh.jobportal.dao.PortalUserDao;
import com.s13sh.jobportal.dto.PortalUser;
import com.s13sh.jobportal.helper.AES;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	@Autowired
	PortalUser portalUser;

	@Autowired
	PortalUserDao userDao;

	public String createAdmin(String email, String password, HttpSession session) {
		if (userDao.existsByEmail(email)) {
			session.setAttribute("failure", "Account Already Exists");
			return "redirect:/";
		} else {
			portalUser.setEmail(email);
			portalUser.setPassword(encrypt(password));
			portalUser.setRole("admin");
			portalUser.setVerified(true);
			userDao.saveUser(portalUser);
			session.setAttribute("success", "Admin Account Created Success");
			return "redirect:/";
		}
	}

	public String encrypt(String password) {
		return AES.encrypt(password, "123");
	}

	public String fetchRecruiters(HttpSession session, ModelMap map) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			List<PortalUser> list = userDao.fetchRecruiters();
			if (list.isEmpty()) {
				session.setAttribute("failure", "No Recruiter Accounts Crearted Yet");
				return "redirect:/";
			} else {
				map.put("list", list);
				return "admin-view-recruiter.html";
			}
		}
	}

	public String completeProfile(int id, HttpSession session) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			PortalUser user = userDao.findUserById(id);
			user.setProfileComplete(true);
			userDao.saveUser(portalUser);
			session.setAttribute("success", "Profile Activated");
			return "redirect:/";
		}
	}

}

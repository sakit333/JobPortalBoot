package com.s13sh.jobportal.service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.s13sh.jobportal.dao.PortalUserDao;
import com.s13sh.jobportal.dto.PortalUser;
import com.s13sh.jobportal.helper.AES;
import com.s13sh.jobportal.helper.EmailSendingHelper;

import jakarta.servlet.http.HttpSession;

@Service
public class PortalUserService {

	@Autowired
	PortalUserDao userDao;

	@Autowired
	EmailSendingHelper emailHelper;

	public String signup(PortalUser portalUser, BindingResult result, HttpSession session) {
		extraValidation(portalUser, result);
		if (result.hasErrors()) {
			return "signup.html";
		} else {
			userDao.deleteIfExists(portalUser.getEmail());
			portalUser.setOtp(generateOtp());
			portalUser.setPassword(encrypt(portalUser.getPassword()));
			portalUser.setConfirm_password(encrypt(portalUser.getConfirm_password()));
			userDao.saveUser(portalUser);
			System.out.println("Data is Saved in Database");
			emailHelper.sendOtp(portalUser);
			session.setAttribute("success", "Otp Sent Success");
			session.setAttribute("id", portalUser.getId());
			return "redirect:/enter-otp";
		}
	}

	public String submitOtp(int otp, int id, HttpSession session) {
		PortalUser portalUser = userDao.findUserById(id);
		if (otp == portalUser.getOtp()) {
			portalUser.setVerified(true);
			userDao.saveUser(portalUser);
			session.setAttribute("success", "Account Created Success");
			session.removeAttribute("failure");
			return "redirect:/login";
		} else {
			session.removeAttribute("success");
			session.setAttribute("failure", "Invalid OTP");
			return "redirect:/enter-otp";
		}
	}

	public String resendOtp(int id, HttpSession session) {
		PortalUser portalUser = userDao.findUserById(id);
		portalUser.setOtp(generateOtp());
		userDao.saveUser(portalUser);
		System.out.println("Data is Updated in database");
		emailHelper.sendOtp(portalUser);
		session.setAttribute("success", "Otp Sent Again, Check");
		return "redirect:/enter-otp";
	}

	public String login(String emph, String password, ModelMap map, HttpSession session) {
		PortalUser portalUser = null;
		try {
			long mobile = Long.parseLong(emph);
			portalUser = userDao.findUserByMobile(mobile);
		} catch (NumberFormatException e) {
			String email = emph;
			portalUser = userDao.findUserByEmail(email);
		}
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Email or Phone Number");
			return "redirect:/login";
		} else {
			if (password.equals(decrypt(portalUser.getPassword()))) {
				if (portalUser.isVerified()) {
					session.setAttribute("success", "Login Success");
					session.setAttribute("portalUser", portalUser);
					if (portalUser.getRole().equals("applicant")) {
						return "redirect:/";
					} else if(portalUser.getRole().equals("recruiter")) {
						return "redirect:/";
					}else {
						return "redirect:/";
					}
				} else {
					session.setAttribute("failure", "First Verify Your Email");
					return "redirect:/login";
				}
			} else {
				session.setAttribute("failure", "Invalid Password");
				return "redirect:/login";
			}
		}

	}

	public String encrypt(String password) {
		return AES.encrypt(password, "123");
	}

	public String decrypt(String password) {
		return AES.decrypt(password, "123");
	}

	public int generateOtp() {
		int otp = new Random().nextInt(100000, 999999);
		System.out.println("Otp Generated - " + otp);
		return otp;
	}

	public void extraValidation(PortalUser portalUser, BindingResult result) {
		if (portalUser.getDob() == null) {
			result.rejectValue("dob", "error.dob", "* Select a Date");
		} else if (LocalDate.now().getYear() - portalUser.getDob().getYear() < 18) {
			result.rejectValue("dob", "error.dob", "* Age should be Greater Than 18");
		}
		if (!portalUser.getPassword().equals(portalUser.getConfirm_password())) {
			result.rejectValue("confirm_password", "error.confirm_password",
					"* Password and Confirm Password Should be Matching");
		}
		if (userDao.existsByEmail(portalUser.getEmail())) {
			result.rejectValue("email", "error.email", "* Account Already Exists");
		}
		if (userDao.existsByMobile(portalUser.getMobile())) {
			result.rejectValue("mobile", "error.mobile", "* Account Already Exists");
		}
	}

}

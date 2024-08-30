package com.s13sh.jobportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.s13sh.jobportal.dao.JobDao;
import com.s13sh.jobportal.dao.PortalUserDao;
import com.s13sh.jobportal.dto.ApplicantDetails;
import com.s13sh.jobportal.dto.Job;
import com.s13sh.jobportal.dto.PortalUser;
import com.s13sh.jobportal.dto.RecruiterDetails;

import jakarta.servlet.http.HttpSession;

@Service
public class RecruiterService {

	@Autowired
	PortalUserDao userDao;
	
	@Autowired
	JobDao jobDao;

	public String saveRecruiterDetails(RecruiterDetails details, HttpSession session, ModelMap map) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			portalUser.setRecruiterDetails(details);
			userDao.saveUser(portalUser);
			session.setAttribute("success", "Profile Updated Success");
			return "redirect:/";
		}
	}

	public String checkProfile(ModelMap map, HttpSession session) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			if (portalUser.getRecruiterDetails() == null) {
				return "recruiter-profile.html";
			} else {
				session.setAttribute("failure", "Wait for Admins Approval");
				return "redirect:/";
			}
		}
	}

	public String postJob(HttpSession session) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			if (!portalUser.isProfileComplete()) {
				session.setAttribute("failure", "First Complete Your Profile");
				return "redirect:/";
			} else {
				return "post-job.html";
			}
		}
	}

	public String postJob(Job job, HttpSession session) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			job.setRecruiterDetails(portalUser.getRecruiterDetails());
			List<Job> jobs = portalUser.getRecruiterDetails().getJobs();
			jobs.add(job);
			userDao.saveUser(portalUser);
			session.removeAttribute("portalUser");
			session.setAttribute("portalUser", portalUser);
			session.setAttribute("success", "Job Posted Success");
			return "redirect:/";
		}
	}

	public String viewJobs(HttpSession session, ModelMap map) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			if (!portalUser.isProfileComplete()) {
				session.setAttribute("failure", "First Complete Your Profile");
				return "redirect:/";
			} else {
			List<Job> jobs = portalUser.getRecruiterDetails().getJobs();
			if (jobs.isEmpty()) {
				session.setAttribute("failure", "No Jobs Posted Yet");
				return "redirect:/";
			} else {
				map.put("jobs", jobs);
				return "recruiter-view-jobs.html";
			}
			}
		}
	}

	public String viewApplicants(int id, HttpSession session, ModelMap map) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "redirect:/";
		} else {
			Job job=jobDao.findById(id);
			List<ApplicantDetails> applicantDetails=job.getApplicantDetails();
			if(applicantDetails.isEmpty()) {
				session.setAttribute("failure", "No Applications Yet");
				return "redirect:/";
			}
			else {
				map.put("applicantDetails", applicantDetails);
				return "view-applicants.html";
			}
		}
	}

}

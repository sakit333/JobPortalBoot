package com.s13sh.jobportal.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.s13sh.jobportal.dao.JobDao;
import com.s13sh.jobportal.dao.PortalUserDao;
import com.s13sh.jobportal.dto.ApplicantDetails;
import com.s13sh.jobportal.dto.Job;
import com.s13sh.jobportal.dto.PortalUser;

import jakarta.servlet.http.HttpSession;

@Service
public class ApplicantService {

	@Autowired
	PortalUserDao userDao;

	@Autowired
	JobDao jobDao;

	public String completeProfile(ApplicantDetails details, MultipartFile resume, HttpSession session, ModelMap map) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "home.html";
		} else {
			String resumePath = uploadToCloudinary(resume);
			details.setUser(portalUser);
			details.setResumePath(resumePath);
			portalUser.setApplicantDetails(details);
			portalUser.setProfileComplete(true);
			userDao.saveUser(portalUser);
			session.setAttribute("success", "Account Verified Success");
			return "redirect:/";
		}
	}

	public String uploadToCloudinary(MultipartFile file) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "djkyoabl5", "api_key",
				"297695696273364", "api_secret", "4bQWA8ZVWVftu83HUe57moGk5Q4"));

		Map resume = null;
		try {
			Map<String, Object> uploadOptions = new HashMap<String, Object>();
			uploadOptions.put("folder", "Resumes");
			resume = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) resume.get("url");
	}

	public String viewJobs(HttpSession session, ModelMap map) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "home.html";
		} else {
			List<Job> jobs = jobDao.viewAllJobs();
			if (jobs.isEmpty()) {
				session.setAttribute("failure", "No Jobs Posted Yet");
				return "redirect:/";
			} else {
				map.put("jobs", jobs);
				return "applicant-view-jobs.html";
			}
		}
	}

	public String applyJob(int id, HttpSession session) {
		PortalUser portalUser = (PortalUser) session.getAttribute("portalUser");
		if (portalUser == null) {
			session.setAttribute("failure", "Invalid Session");
			return "home.html";
		} else {
			if (!portalUser.isProfileComplete()) {
				session.setAttribute("failure", "First Complete Your Profile");
				return "redirect:/";
			} else {
				Job job = jobDao.findById(id);
				ApplicantDetails applicantDetails = portalUser.getApplicantDetails();
				List<Job> appliedJobs = applicantDetails.getJobs();
				boolean applied = false;
				for (Job appliedJob : appliedJobs) {
					if (job.getId() == appliedJob.getId()) {
						applied = true;
						break;
					}
				}

				if (applied) {
					session.setAttribute("failure", "Already Applied Wait for Response or Contact - "
							+ job.getRecruiterDetails().getCompanyMobileNumber());
					return "redirect:/";
				} else {
					job.getApplicantDetails().add(applicantDetails);
					appliedJobs.add(job);
					userDao.saveUser(portalUser);
					jobDao.saveJob(job);	
					session.setAttribute("success", "Applied for Job Success Wait for Response");
					return "redirect:/";
				}
			}
		}
	}

}

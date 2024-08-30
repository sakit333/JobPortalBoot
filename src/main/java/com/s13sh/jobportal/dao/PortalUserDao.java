package com.s13sh.jobportal.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.s13sh.jobportal.dto.PortalUser;
import com.s13sh.jobportal.repository.PortalUserRepository;

@Repository
public class PortalUserDao {

	@Autowired
	PortalUserRepository userRepository;

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmailAndVerifiedTrue(email);
	}

	public void saveUser(PortalUser portalUser) {
		userRepository.save(portalUser);
	}

	public PortalUser findUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	public void deleteIfExists(String email) {
		PortalUser user = userRepository.findByEmail(email);
		if (user != null)
			userRepository.delete(user);
	}

	public PortalUser findUserByMobile(long mobile) {
		return userRepository.findByMobile(mobile);
	}

	public PortalUser findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean existsByMobile(long mobile) {
		return userRepository.existsByMobileAndVerifiedTrue(mobile);
	}

	public List<PortalUser> fetchRecruiters() {
		return userRepository.findByRecruiterDetailsNotNull();
	}
}

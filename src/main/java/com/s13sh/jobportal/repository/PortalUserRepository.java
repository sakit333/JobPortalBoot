package com.s13sh.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.s13sh.jobportal.dto.PortalUser;

public interface PortalUserRepository extends JpaRepository<PortalUser,Integer>
{

	boolean existsByEmailAndVerifiedTrue(String email);

	PortalUser findByEmail(String email);

	PortalUser findByMobile(long mobile);

	boolean existsByMobileAndVerifiedTrue(long mobile);

	List<PortalUser> findByRecruiterDetailsNotNull();

}

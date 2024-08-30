package com.s13sh.jobportal.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Component
@Data
@Entity
public class ApplicantDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String[] skills;
	private String highestEducation;
	private double percentage10;
	private double percentage12;
	private double percentageDegree;
	private double percentageMasters;
	private String resumePath;
	
	public String skillsString() {
		return Arrays.toString(skills);
	}

	@OneToMany(fetch = FetchType.EAGER)
	List<Job> jobs = new ArrayList<Job>();
	
	@OneToOne
	PortalUser user;

}

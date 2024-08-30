package com.s13sh.jobportal.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String jobProfile;
	private String salary;
	private int experienceRequired;
	private String skillsRequired;
	private String jobDescription;
	private LocalDate lastDate;
	
	@ManyToOne
	RecruiterDetails recruiterDetails;
	
	@OneToMany(fetch = FetchType.EAGER)
	List<ApplicantDetails> applicantDetails=new ArrayList<ApplicantDetails>();

}

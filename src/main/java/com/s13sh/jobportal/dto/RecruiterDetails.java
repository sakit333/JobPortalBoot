package com.s13sh.jobportal.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@Component
public class RecruiterDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String companyName;
	private String companyAddress;
	private long companyMobileNumber;
	private String licenceNumber;
	private String aboutUs;
	private boolean approved;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<Job> jobs = new ArrayList<Job>();
}

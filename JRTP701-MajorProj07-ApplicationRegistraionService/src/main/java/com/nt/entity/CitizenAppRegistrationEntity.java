package com.nt.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="CITIZEN_APPLICATION")
@AllArgsConstructor
@NoArgsConstructor
public class CitizenAppRegistrationEntity {

	@Id
	@SequenceGenerator(name="citizen_gen",sequenceName = "app_id_seq",initialValue = 1000,allocationSize = 1)
	@GeneratedValue(generator = "citizen_gen",strategy = GenerationType.SEQUENCE)
	private Integer appId;
	@Column(length=30)
	private String fullName;
	@Column(length=30)
	private String email;
	private String gender;
	private Long phoneNo;
	private Long ssn;
	private String stateName;
	private LocalDate dob;
	@Column(length=30)
	private String createdBy;
	@Column(length=30)
	private String updatedBy;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDate creationDate;
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDate updationDate;
}

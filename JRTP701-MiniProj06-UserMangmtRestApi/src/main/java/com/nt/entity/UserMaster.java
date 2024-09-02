package com.nt.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="JRTP_USER_MASTER")
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	@Column(length = 30)
	private String name;
	@Column(length =30,unique=true,nullable=false)
	private String email;
	@Column(length = 20)
	private String password;
	private Long mobileNo;
	private Long aadharNo;
	@Column(length = 10)
	private String gender;
	private LocalDate dob;
	private String activeSW;
	
	//MetaData
	@CreationTimestamp
	@Column(updatable = false,insertable = true)
	private LocalDateTime createdOn;
	@UpdateTimestamp
	@Column(updatable = true,insertable = false)
	private LocalDateTime updatedOn;
	@Column(length=20)
	private String createdBy;
	@Column(length = 20)
	private String updatedBy;
	
}

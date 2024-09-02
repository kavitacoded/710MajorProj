package com.nt.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CourseDetailsTBL")
@Entity
public class CourseDetails {

	@Id
	@GeneratedValue
	private Integer courseId;
	@Column(length=50)
	private String courseName;
	private String location;
	private String courseCategory;
	private String facultyName;
	private Double fee;
	private String adminName;
	 private Long adminContact;
	 private String trainingMode;
	 private LocalDateTime startDate;
	 private String courseStatus;
	 @CreationTimestamp
	 @Column(insertable = false,updatable = false)
	 private LocalDateTime creationTime;
	 @UpdateTimestamp
	 @Column(insertable = false,updatable = true)
	 private LocalDateTime updationTime;
	private String createdBy;
	private String updatedBy;
	
}

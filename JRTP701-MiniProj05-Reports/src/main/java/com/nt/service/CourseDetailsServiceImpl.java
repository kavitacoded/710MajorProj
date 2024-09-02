package com.nt.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.repository.ICourseDetailsRepository;

@Service
public class CourseDetailsServiceImpl implements ICourseDetailsService {

	
	@Autowired
	private ICourseDetailsRepository courseRepo;
	
	@Override
	public Set<String> getCourseCategories() {
		return courseRepo.getUniqueCourseCategories();
	}

}

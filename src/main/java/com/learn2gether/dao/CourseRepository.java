package com.learn2gether.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learn2gether.domain.Course;

public interface CourseRepository extends CrudRepository<Course,Integer>{

}

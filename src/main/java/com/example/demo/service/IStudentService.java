package com.example.demo.service;

import com.example.demo.model.Student;

import java.util.List;

public interface IStudentService {
    Student newStudent(int id,String name);
    void addGrade(int id,int n,int grade);
    List<Student> getStudentList();
    double[] ave();
    Student[] max();
    Student[] min();
    List<Student> passed();
}

package com.example.demo.service.impl;

import com.example.demo.model.Student;
import com.example.demo.service.IStudentService;

import java.util.ArrayList;
import java.util.List;

public class StudentService implements IStudentService {

    private static StudentService studentService = new StudentService();
    private StudentService(){
    }

    public static StudentService getInstance(){
        return studentService;
    }

    private static List<Student> studentList = new ArrayList<>();

    @Override
    public Student newStudent(int id,String name) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        studentList.add(student);
        return student;
    }

    @Override
    public void addGrade(int id, int n, int grade) {
        for(Student student : studentList){
            if(student.getId() == id){
                switch(n){
                    case 1:
                        student.setClass1Grade(grade);
                        break;
                    case 2:
                        student.setClass2Grade(grade);
                        break;
                    case 3:
                        student.setClass3Grade(grade);
                        break;
                    case 4:
                        student.setClass4Grade(grade);
                        break;
                }
            }
        }
    }

    @Override
    public List<Student> getStudentList() {
        return studentList;
    }

    @Override
    public double[] ave() {
        double[] sum = new double[4];
        int[] num = new int[4];
        double[] ave =new double[4];
        for(Student student : studentList){
            if(student.getClass1Grade() != 0){
                sum[0] = sum[0] + student.getClass1Grade();
                num[0]++;
            }
            if(student.getClass2Grade() != 0){
                sum[1] = sum[1] + student.getClass2Grade();
                num[1]++;
            }
            if(student.getClass3Grade() != 0){
                sum[2] =sum[2] + student.getClass3Grade();
                num[2]++;
            }
            if(student.getClass4Grade() != 0){
                sum[3] =sum[3] + student.getClass4Grade();
                num[3]++;
            }
        }for(int i = 0;i < sum.length;i++){
            if(num[i] == 0){
                ave[i] = 0;
            }
            ave[i] = sum[i] / num[i];
        }
        return ave;
    }

    @Override
    public Student[] max() {
        Student[] max = new Student[4];
        for(Student student : studentList){
            if(max[0] == null){
                max[0] = student;
            }else if(max[0].getClass1Grade() < student.getClass1Grade()){
                max[0] =student;
            }
            if(max[1] == null){
                max[1] = student;
            }else if(max[0].getClass2Grade() < student.getClass2Grade()){
                max[1] =student;
            }
            if(max[2] == null){
                max[2] = student;
            }else if(max[2].getClass3Grade() < student.getClass3Grade()){
                max[2] =student;
            }
            if(max[3] == null){
                max[3] = student;
            }else if(max[3].getClass4Grade() < student.getClass4Grade()){
                max[3] =student;
            }
        }
        return max;
    }

    @Override
    public Student[] min() {
        Student[] min = new Student[4];
        for(Student student : studentList){
            if(min[0] == null){
                min[0] = student;
            }else if(min[0].getClass1Grade() > student.getClass1Grade()){
                min[0] =student;
            }
            if(min[1] == null){
                min[1] = student;
            }else if(min[0].getClass2Grade() > student.getClass2Grade()){
                min[1] =student;
            }
            if(min[2] == null){
                min[2] = student;
            }else if(min[2].getClass3Grade() > student.getClass3Grade()){
                min[2] =student;
            }
            if(min[3] == null){
                min[3] = student;
            }else if(min[3].getClass4Grade() > student.getClass4Grade()){
                min[3] =student;
            }
        }
        return min;
    }

    @Override
    public List<Student> passed() {
        List<Student> passStudentList = new ArrayList<>();
        for(Student student : studentList){
            if(student.getClass1Grade() >= 90
                    && student.getClass2Grade() >= 90
                    && student.getClass3Grade() >= 90
                    && student.getClass4Grade() >=90){
                passStudentList.add(student);
            }
        }
        return passStudentList;
    }
}

package com.newspoint.demo.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private Integer id;
    private String first_name;
    private String last_name;
    @Column(name = "birth_date")
    private String birthDate;
    private String phone_no;
    private Integer age;


    public User() {}

    public User(String first_name, String last_name, String birthDate, String phone_no) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthDate = birthDate;
        this.phone_no = phone_no;
    }

    public User(String first_name, String last_name, String birthDate) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name.substring(0, 1).toUpperCase() + first_name.substring(1).toLowerCase();
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {

        return last_name.substring(0, 1).toUpperCase() + last_name.substring(1).toLowerCase();
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

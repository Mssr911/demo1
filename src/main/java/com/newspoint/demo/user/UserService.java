package com.newspoint.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }


    public List<User> parseDocument(MultipartFile file) {

        List<String> stringList = multiToList(file);
        List<User> newUserList = new ArrayList<>();
        List<User> userList = repository.findAll();

        for (int i = 1; i <= stringList.size() - 1; i++) {
            {
                String s = stringList.get(i);
                String[] user = s.split(";");

                if (user.length == 3 || (user.length == 4 && !isNumeric(user[3]))) {
                    newUserList.add(new User(user[0].trim(), user[1].trim(), user[2].replace('.', '-').trim()));
                    logger.warn(user[0].trim() + " " + user[1].trim() + " - invalid phone number.");
                } else if (user.length == 4 &&
                        isNumeric(user[3]) &&
                        !checkPhoneNumber(user[3], userList) &&
                        !checkPhoneNumber(user[3], newUserList)) {

                    newUserList.add(new User(user[0].trim(), user[1].trim(), user[2].replace('.', '-').trim(), user[3].trim()));

                } else if (user.length == 4 &&
                        isNumeric(user[3]) &&
                        checkPhoneNumber(user[3], userList)) {

                    logger.warn("User: " + user[0].trim() + " " + user[1].trim() + " not saved: phone number "
                            + user[3].trim() + " already exists in database.");
                } else if (user.length == 4 &&
                        isNumeric(user[3]) && checkPhoneNumber(user[3], newUserList)) {
                    newUserList.add(new User(user[0].trim(), user[1].trim(), user[2].replace('.', '-').trim()));
                    logger.warn("User: " + user[0].trim() + " " + user[1].trim() + " saved without phone number: "
                            + user[3].trim() + " exists in database.");
                }

            }
        }

        return newUserList;
    }


    public List<String> multiToList(MultipartFile file) {
        BufferedReader br;
        List<String> result = new ArrayList<>();
        try {

            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            logger.error("Failed to convert the downloaded file into a list.");
        }

        return result;
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Boolean checkPhoneNumber(String number, List<User> userList) {
        for (User u : userList) {
            if (Optional.ofNullable(u.getPhoneNo()).orElse("nonum").equals(number)) {
                return true;
            }
            java.sql.Date sqlDateNow = new java.sql.Date(new java.util.Date().getTime());
        }
        return false;
    }

    public int setAgeForAllUsers(List<User> userList) {

        try {

            for (User u : userList) {

                String date = u.getBirthDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatY = new SimpleDateFormat("yyyy");
                SimpleDateFormat dateFormatM = new SimpleDateFormat("MM");
                Calendar calendar = Calendar.getInstance();

                // dzisiejsza data
                String timeString1 = dateFormatY.format(calendar.getTime());
                String timeString2 = dateFormatM.format(calendar.getTime());
                int yearToday = Integer.parseInt(timeString1);
                int monthToday = Integer.parseInt(timeString2);

                // data urodzenia
                Date date1 = dateFormat.parse(date);
                String timeString3 = dateFormatY.format(date1.getTime());
                String timeString4 = dateFormatM.format(date1.getTime());
                int yearOfBirth = Integer.parseInt(timeString3);
                int monthOfBirth = Integer.parseInt(timeString4);

                int age = yearToday - yearOfBirth;

                if (monthOfBirth <= monthToday) {
                    u.setAge(age);
                } else {
                    u.setAge(age - 1);
                }


            }
        } catch (ParseException e) {
            logger.error("Parsing to int ipmossible.");
        }
        return 0;
    }


}

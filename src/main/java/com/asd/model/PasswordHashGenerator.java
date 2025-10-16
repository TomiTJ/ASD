package com.asd.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminHash = encoder.encode("admin123");
        String viewerHash = encoder.encode("viewer123");
        String LesanduHash = encoder.encode("lesandu123");


        System.out.println("admin123 -> " + adminHash);
        System.out.println("viewer123 -> " + viewerHash);
        System.out.println("Lesandu123 -> " + LesanduHash);

    }

}

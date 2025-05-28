package com.example.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SecurityTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncoder() {

        // 원 비밀번호 : rawpassword
        String password = "1111";
        // passwordEncoder.encode(원비밀번호) : 암호화
        String encodePassword = passwordEncoder.encode(password);
        // password 1111 , encodePassword :
        // {bcrypt}$2a$10$I9TUsu9Pkjhytd7l3RKkVu/Uwsf2FA3Y.BX/AX8YpdSfZy.pbIrPi
        System.out.println("password " + password + " , " + "encodePassword : " + encodePassword);

        System.out.println("비밀번호 오류 " + passwordEncoder.matches("2222", encodePassword));
        System.out.println(passwordEncoder.matches("1111", encodePassword));
    }

}

package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 curl --location 'http://127.0.0.1:8080/login' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --header 'Cookie: JSESSIONID=0FFBCAF713972CB7BE0F8C3F8B8C2EBB' \
 --data-urlencode 'username=admin' \
 --data-urlencode 'password=123456'

 curl --location 'http://127.0.0.1:8080/index' \
 --header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHBpcmVzSW4iOjE3MTA2MTIyOTM3NDQsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST0xFX2FkbWluIn0seyJhdXRob3JpdHkiOiJST0xFX3VzZXIifV0sImVuYWJsZWQiOnRydWUsInVzZXJuYW1lIjoiYWRtaW4ifQ.N4bs9hCasJg234epePR1PKSJKUnb_junckFsU3GvlLw' \
 --header 'Cookie: JSESSIONID=2E05E4AE73692B9398BB5566FCB5AC92'
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class JwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }
}
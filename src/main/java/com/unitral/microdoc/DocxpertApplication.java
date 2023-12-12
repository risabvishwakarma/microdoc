package com.unitral.microdoc;

import com.unitral.microdoc.entity.UserAuthentication;
import com.unitral.microdoc.repository.UserAuthenticationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DocxpertApplication {
	public static void main(String[] args) {
		SpringApplication.run(DocxpertApplication.class, args);
	}

}

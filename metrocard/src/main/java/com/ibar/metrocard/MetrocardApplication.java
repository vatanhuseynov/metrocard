package com.ibar.metrocard;

import com.ibar.metrocard.user.model.Role;
import com.ibar.metrocard.user.model.User;
import com.ibar.metrocard.user.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class MetrocardApplication {
	private final RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(MetrocardApplication.class, args);
	}
		@PostConstruct
		void insertDefaultRole() {
			var user = Role.builder().name("İstifadəçi").keyword("user").build();
			var admin = Role.builder().name("Admin").keyword("admin").build();

			if (!roleRepository.existsByKeyword(user.getKeyword())) {
				roleRepository.save(user);
			}

			if (!roleRepository.existsByKeyword(admin.getKeyword())) {
				roleRepository.save(admin);

			}
		}

}
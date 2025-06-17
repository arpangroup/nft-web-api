package com.trustai.nft_app;

import com.trustai.user_service.user.entity.Kyc;
import com.trustai.user_service.user.entity.User;
import com.trustai.user_service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

@SpringBootApplication
@ComponentScan(basePackages = {"com.trustai.*"})
public class NftCoreApplication implements CommandLineRunner {
	@Autowired UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(NftCoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = userRepository.findById(1L).orElse(null);

		if (user != null) return;
		User rootUser = new User("U1", 3, BigDecimal.ZERO);
		rootUser.setKycInfo(new Kyc());
		userRepository.save(rootUser);
	}
}

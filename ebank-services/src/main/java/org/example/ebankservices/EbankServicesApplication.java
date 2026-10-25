package org.example.ebankservices;

import org.example.ebankservices.entities.BankAccount;
import org.example.ebankservices.repository.BankAccountRepository;
import org.example.ebankservices.services.EbankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EbankServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServicesApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EbankService ebankService) {
        return args -> {
            for(int i=1; i<=3 ; i++){
                for(int j=0; j<5 ; j++){
                    ebankService.save(BankAccount.builder()
                                    .type(Math.random()>0.5? "CURRENT-ACCOUNT":"SAVING-ACCOUNT")
                                    .balance(1000+Math.random()*60000)
                                    .customerId(i)
                            .build());
                }
            }
        };
    }

}

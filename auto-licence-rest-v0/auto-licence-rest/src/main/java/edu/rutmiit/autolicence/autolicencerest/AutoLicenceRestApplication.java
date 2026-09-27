package edu.rutmiit.autolicence.autolicencerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(
        scanBasePackages = {"edu.rutmiit.autolicence.autolicencerest", "edu.rutmiit.autolicence.candidatesapicontract", "edu.rutmiit.autolicence.events"},
        exclude = {DataSourceAutoConfiguration.class}
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class AutoLicenceRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoLicenceRestApplication.class, args);
    }

}

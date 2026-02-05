//package com.ecom.Shopping_cart;
//
//import org.springframework.core.env.Environment;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ShoppingCartApplication {
//
//
//	public static void main(String[] args) {
//		SpringApplication.run(ShoppingCartApplication.class, args);
//
//	}
//}


package com.ecom.Shopping_cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ShoppingCartApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =
				SpringApplication.run(ShoppingCartApplication.class, args);

		Environment env = context.getEnvironment();

		System.out.println("DB URL = " + env.getProperty("spring.datasource.url"));
		System.out.println("DB USER = " + env.getProperty("spring.datasource.username"));
	}
}

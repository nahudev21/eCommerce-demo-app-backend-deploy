package com.nahudev.electronic_shop;

import com.nahudev.electronic_shop.controller.MercadoPagoController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;

@SpringBootApplication
public class ElectronicShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicShopApplication.class, args);
	}

}

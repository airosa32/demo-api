package com.airosa.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	// Explicações estao no User.java
	/*
	 * Postman -> simulador de navegador, uso de rotas esta,
	 * nele, no springboot pasta.
	 * 
	 * Tudo que é inserido na barra do navegador, eo metodo
	 * GET, para POST PUT DELETE... usa-se javascript ou
	 * alguma ferramenta, ou na caso atravez do postman,
	 * ferramenta de automação para testes de rota.
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

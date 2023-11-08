package br.com.spolador.desafiofipe;

import br.com.spolador.desafiofipe.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioFipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DesafioFipeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeDadosAPI();
	}
}

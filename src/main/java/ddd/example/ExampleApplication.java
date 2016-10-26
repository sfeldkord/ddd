package ddd.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import ddd.example.atm.Atm;
import ddd.example.atm.AtmRepository;
import ddd.example.shared.Euros;

@SpringBootApplication
public class ExampleApplication {

	public static void main(String... args) {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ExampleApplication.class).run();
			AtmRepository atmRepo = ctx.getBean(AtmRepository.class);

			Atm atm = new Atm();
			atm.load(Euros.ONE_EURO);
			atm = atmRepo.save(atm);
			atmRepo.getAtmList().forEach(System.out::println);
		ctx.close();
		System.exit(0);
	}

}

package ddd.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import ddd.example.atm.Atm;
import ddd.example.atm.AtmRepository;
import ddd.example.shared.Euros;

@SpringBootApplication
public class ExampleApplication {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ExampleApplication.class, args);
		
		AtmRepository atmRepo = ctx.getBean(AtmRepository.class);
		
		Atm atm = new Atm();
		atm.load(Euros.ONE_EURO);
		atm = atmRepo.save(atm);
		atmRepo.getAtmList().forEach(a -> System.out.println(a));
	}

}

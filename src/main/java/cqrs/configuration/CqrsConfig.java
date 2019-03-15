package cqrs.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cqrs")
public class CqrsConfig {
	// configuration to let spring know, where to scan for @Components
}

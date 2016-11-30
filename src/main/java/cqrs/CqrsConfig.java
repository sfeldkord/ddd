package cqrs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CqrsConfig.class)
public class CqrsConfig {
	// configuration to let spring know, where to scan for @Components
}

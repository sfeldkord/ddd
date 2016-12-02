DDD-Framework

The framework is inspired by Eric Evan's book on Domain-Driven Design (http://dddcommunity.org/book/evans_2003/).
The implementation and examples have been adapted from ideas presented by Vladimir Khorikov in his Pluralsight course 'Domain-Driven Design in Practice' (https://www.pluralsight.com/courses/domain-driven-design-in-practice).

Furthermore, I adapted code from Uwe Schäfers (https://github.com/uweschaefer/es-basics) and Gregory Young (https://github.com/gregoryyoung/m-r) to implement and test the CQRS and Event Sourcing parts.

Project Lombok (https://projectlombok.org/) is used to compile the code. Thus it has to be on the classpath during compilation. Gradle takes care of this but you have to install Lombok onto your IDE if you want it building your code. 
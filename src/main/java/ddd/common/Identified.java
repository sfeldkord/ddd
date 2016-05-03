package ddd.common;

//Sehr ähnlich zu org.springframework.data.domain.Persistable
public interface Identified<ID> {

	ID getId();
	
}
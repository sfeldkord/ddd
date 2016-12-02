package cqrs.common;


//replace by @javax.persistence.Entiy???
public interface Entity<ID> {
	
	//annotate with ???
	//- org.springframework.data.annotation.Id (favor)
	//- javax.persistence.GeneratedValue
	//- javax.persistence.Id
	//question type - long?
	ID getId();//TODO Always use UUID?
	//getAggregateId() ??? reverse direction
	
	//Status ACTIVE and ARCHIVE (logically removed, pre-aggregated, ...?)
}

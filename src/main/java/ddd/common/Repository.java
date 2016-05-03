package ddd.common;

//Similar to CrudRepository etc.
public interface Repository<E extends Entity<ID>, ID> {

	E save(E entity);
	
//	E findById(ID id);
//	
//	E delete(E entity);
}

package ddd.example.atm;

import java.util.List;

import ddd.common.Repository;

public interface AtmRepository extends Repository<Atm, Long> {

	Atm save(Atm atm);
	
	List<Atm> getAtmList();
	
}

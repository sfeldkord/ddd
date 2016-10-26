package ddd.example.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ddd.example.atm.Atm;
import ddd.example.atm.AtmRepository;

@Repository
@Transactional(readOnly = true)
public class AtmRepositoryImpl implements AtmRepository {

	@Autowired
	private EntityManager em;
	
	@Override
	@Transactional
	public Atm save(Atm atm) {
		em.persist(atm);
		return atm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Atm> getAtmList() {
		Query query = em.createQuery("SELECT a FROM Atm a");
		return query.getResultList();
	}
	
}

package ddd.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.PrePersist;

import ddd.common.Entity;

public class EntityListener {

	@GeneratedValue
	@PrePersist
	public void prePersist(Entity<?> entity) {
		if (entity.getId() == null) {
			//DO nothing
		}
	}

}

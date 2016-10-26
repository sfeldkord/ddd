package ddd.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.PrePersist;

import ddd.common.Identified;

public class IdentifiedEntityListener {

	@GeneratedValue
	@PrePersist
	public void prePersist(Identified<?> identified) {
		if (identified.getId() == null) {
			//DO nothing
		}
	}

}

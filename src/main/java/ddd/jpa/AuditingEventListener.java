package ddd.jpa;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import ddd.common.Auditable;

//TODO set auditing
public class AuditingEventListener {

	@PrePersist
	public void prePersist(Auditable auditable) {
		// Date now = Date.from(Instant.now());
		// String user = "default";
		//
		// entity.setDefTime(getCurrentTimestamp());
		// entity.setDefUser(user);

		// entity.setModTime(getCurrentTimestamp());
		// entity.setModUser(user);
	}

	@PreUpdate
	public void preUpdate(Auditable auditable) {
		// Date now = Date.from(Instant.now());
		// String user = "default";
		//
		// entity.setModTime(getCurrentTimestamp());
		// entity.setModUser(user);
	}

}

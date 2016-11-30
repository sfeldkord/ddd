package cqrs.example.foobank;

import java.util.UUID;

public interface AccountRepository {
	Account find(UUID id);

}

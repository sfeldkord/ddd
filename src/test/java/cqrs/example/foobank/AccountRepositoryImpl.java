package cqrs.example.foobank;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cqrs.EventStore;
import cqrs.example.foobank.view.AccountView;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountRepositoryImpl implements AccountRepository {
	final EventStore es;

	@Override
	public Account find(UUID id) {
		return new AccountView(es, id).getAccount();
	}

}

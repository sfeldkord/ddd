package cqrs.example.foobank;

import java.util.UUID;

import org.springframework.stereotype.Component;

import cqrs.example.foobank.view.AccountView;
import cqrs.store.EventStore;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
	
	final EventStore es;

	@Override
	public Account find(UUID id) {
		return new AccountView(es, id).getAccount();
	}

}

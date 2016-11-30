package cqrs.example.foobank.service;

import java.util.UUID;

public interface CreditNotificationService {

	public void sendEmailTo(UUID accountId, int amountRecieved);

}

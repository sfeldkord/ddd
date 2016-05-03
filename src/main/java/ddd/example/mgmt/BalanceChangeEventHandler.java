package ddd.example.mgmt;

import ddd.example.atm.BalanceChangedEvent;

public class BalanceChangeEventHandler {

	HeadOfficeRepository headOfficeRepo;

    public void handle(BalanceChangedEvent event)
    {
        HeadOffice headOffice = HeadOffice.INSTANCE;
        headOffice.changeBalance(event.delta);
        headOfficeRepo.save(headOffice);
    }

}

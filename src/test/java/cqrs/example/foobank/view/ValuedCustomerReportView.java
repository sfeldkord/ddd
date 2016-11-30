package cqrs.example.foobank.view;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import cqrs.EventStore;
import cqrs.event.EventConsumer;
import cqrs.event.PullView;
import cqrs.example.foobank.event.DepositedEvent;

public class ValuedCustomerReportView extends PullView {

    public ValuedCustomerReportView(EventStore es) {
        super(es);
    }

    final Set<UUID> candidates = new HashSet<>();

    final Set<UUID> valuedCustomers = new HashSet<>();

    @EventConsumer
    public void apply(DepositedEvent evt) {
        if (evt.getAmount() >= 1000) {

            UUID accountId = evt.getAggregateId();

            if (isValuedCustomer(accountId))
                // all done
                return;

            if (candidates.contains(accountId)) {
                // promote to valued
                candidates.remove(accountId);
                valuedCustomers.add(accountId);
            } else
                candidates.add(accountId);
        }
    }

    public final Collection<UUID> getValuedCustomers() {
        pullEvents();
        return Collections.unmodifiableSet(valuedCustomers);
    }

    public final boolean isValuedCustomer(UUID accountId) {
        return getValuedCustomers().contains(accountId);
    }

}

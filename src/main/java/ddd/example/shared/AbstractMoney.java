package ddd.example.shared;

import java.util.*;
import java.util.stream.Collectors;

import ddd.common.AbstractValueObject;

@SuppressWarnings("serial")
public abstract class AbstractMoney<CUR extends AbstractMoney<CUR>> extends AbstractValueObject implements Money<CUR> {

	//ListOfUnits
	//SortedMap<Unit, amount>
	
	public AbstractMoney() {
		super();
	}

	protected List<CUR> allocate(int remainderInCent, List<CUR> availableCoins, List<CUR> partialResult) {
		if (remainderInCent == 0) {
			return partialResult;
		} else if (remainderInCent < 0) {
			return Collections.emptyList();
		} else if (availableCoins.isEmpty()) {
			return Collections.emptyList();
		} else {
			CUR coin = availableCoins.get(0);
			List<CUR> rest = removeHead(availableCoins);
			List<CUR> result = allocate(remainderInCent - coin.amountInCent(), rest,
					add(coin, partialResult));
			if (result.isEmpty()) {
				return allocate(remainderInCent, rest, partialResult);
			} else {
				return result;
			}
		}
	}

	private List<CUR> removeHead(List<CUR> coins) {
		return coins.stream().skip(1).collect(Collectors.toList());
	}

	private List<CUR> add(CUR head, List<CUR> tail) {
		List<CUR> result = new ArrayList<>();
		result.add(head);
		tail.forEach(coin -> result.add(coin));
		return result;
	}

	protected abstract int amountInCent();
	
}
package ddd.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.*;

@SuppressWarnings("serial")
public abstract class AbstractValueObject implements ValueObject, Serializable {
	
	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public final boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}

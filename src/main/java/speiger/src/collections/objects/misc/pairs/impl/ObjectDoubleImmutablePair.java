package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectDoublePair;

/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectDoubleImmutablePair<T> implements ObjectDoublePair<T>
{
	protected final T key;
	protected final double value;
	
	/**
	 * Default Constructor
	 */
	public ObjectDoubleImmutablePair() {
		this(null, 0D);
	}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectDoubleImmutablePair(T key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectDoublePair<T> setKey(T key) {
		return new ObjectDoubleImmutablePair<>(key, value);
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectDoublePair<T> setDoubleValue(double value) {
		return new ObjectDoubleImmutablePair<>(key, value);
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public ObjectDoublePair<T> set(T key, double value) {
		return new ObjectDoubleImmutablePair<>(key, value);
	}
	
	@Override
	public ObjectDoublePair<T> shallowCopy() {
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ObjectDoublePair) {
			ObjectDoublePair<T> entry = (ObjectDoublePair<T>)obj;
			return Objects.equals(key, entry.getKey()) && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(key) ^ Double.hashCode(value);
	}
	
	@Override
	public String toString() {
		return Objects.toString(key) + "->" + Double.toString(value);
	}
}
package speiger.src.collections.objects.misc.pairs.impl;

import java.util.Objects;

import speiger.src.collections.objects.misc.pairs.ObjectDoublePair;



/**
 * Mutable Pair Implementation that
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectDoubleMutablePair<T> implements ObjectDoublePair<T>
{
	protected T key;
	protected double value;
	
	/**
	 * Default Constructor
	 */
	public ObjectDoubleMutablePair() {}
	
	/**
	 * Key/Value Constructur
	 * @param key the key of the Pair
	 * @param value the value of the Pair
	 */
	public ObjectDoubleMutablePair(T key, double value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public ObjectDoublePair<T> setKey(T key) {
		this.key = key;
		return this;
	}
	
	@Override
	public T getKey() {
		return key;
	}
	
	@Override
	public ObjectDoublePair<T> setDoubleValue(double value) {
		this.value = value;
		return this;
	}
	
	@Override
	public double getDoubleValue() {
		return value;
	}
	
	@Override
	public ObjectDoublePair<T> set(T key, double value) {
		this.key = key;
		this.value = value;
		return this;
	}
	
	@Override
	public ObjectDoublePair<T> shallowCopy() {
		return ObjectDoublePair.mutable(key, value);
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
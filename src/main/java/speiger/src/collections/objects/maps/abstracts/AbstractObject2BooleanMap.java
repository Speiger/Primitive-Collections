package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.objects.functions.function.ObjectBooleanUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2BooleanMaps;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObject2BooleanMap<T> extends AbstractMap<T, Boolean> implements Object2BooleanMap<T>
{
	protected boolean defaultReturnValue = false;
	
	@Override
	public boolean getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2BooleanMap<T> setDefaultReturnValue(boolean v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Object2BooleanMap.Entry<T>> getFastIterable(Object2BooleanMap<T> map) {
		return Object2BooleanMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Object2BooleanMap.Entry<T>> getFastIterator(Object2BooleanMap<T> map) {
		return Object2BooleanMaps.fastIterator(map);
	}
	
	@Override
	public Object2BooleanMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Boolean put(T key, Boolean value) {
		return Boolean.valueOf(put(key, value.booleanValue()));
	}
	
	@Override
	public void putAll(Object2BooleanMap<T> m) {
		for(ObjectIterator<Object2BooleanMap.Entry<T>> iter = getFastIterator(m);iter.hasNext();) {
			Object2BooleanMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getBooleanValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Boolean> m)
	{
		if(m instanceof Object2BooleanMap) putAll((Object2BooleanMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Object2BooleanMap<T> m) {
		for(Object2BooleanMap.Entry<T> entry : getFastIterable(m))
			putIfAbsent(entry.getKey(), entry.getBooleanValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(BooleanIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextBoolean() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, boolean oldValue, boolean newValue) {
		boolean curValue = getBoolean(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public boolean replace(T key, boolean value) {
		boolean curValue;
		if ((curValue = getBoolean(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBooleans(Object2BooleanMap<T> m) {
		for(Object2BooleanMap.Entry<T> entry : getFastIterable(m))
			replace(entry.getKey(), entry.getBooleanValue());
	}
	
	@Override
	public void replaceBooleans(ObjectBooleanUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2BooleanMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2BooleanMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsBoolean(entry.getKey(), entry.getBooleanValue()));
		}
	}

	@Override
	public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean newValue = mappingFunction.applyAsBoolean(key, getBoolean(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public boolean computeBooleanNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value = getBoolean(key);
		boolean newValue = mappingFunction.applyAsBoolean(key, value);
		if(newValue == getDefaultReturnValue()) {
			if(value != getDefaultReturnValue() || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(T key, Predicate<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean computeBooleanIfAbsentNonDefault(T key, Predicate<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = getBoolean(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean supplyBooleanIfAbsentNonDefault(T key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		boolean value;
		if((value = getBoolean(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, getBoolean(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = getBoolean(key)) != getDefaultReturnValue() || containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean oldValue = getBoolean(key);
		boolean newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2BooleanMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			boolean oldValue = getBoolean(key);
			boolean newValue = oldValue == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(oldValue, entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Boolean get(Object key) {
		return Boolean.valueOf(getBoolean((T)key));
	}
	
	@Override
	public Boolean getOrDefault(Object key, Boolean defaultValue) {
		Boolean value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Boolean remove(Object key) {
		return Boolean.valueOf(rem((T)key));
	}
	
	@Override
	public void forEach(ObjectBooleanConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2BooleanMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2BooleanMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getBooleanValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				if(AbstractObject2BooleanMap.this.containsKey(o)) {
					AbstractObject2BooleanMap.this.remove(o);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean add(T o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ObjectIterator<T> iterator() {
				return new ObjectIterator<T>() {
					ObjectIterator<Object2BooleanMap.Entry<T>> iter = getFastIterator(AbstractObject2BooleanMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public T next() {
						return iter.next().getKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractObject2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2BooleanMap.this.clear();
			}
		};
	}

	@Override
	public BooleanCollection values() {
		return new AbstractBooleanCollection() {
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractObject2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2BooleanMap.this.clear();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Object2BooleanMap.Entry<T>> iter = getFastIterator(AbstractObject2BooleanMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public boolean nextBoolean() {
						return iter.next().getBooleanValue();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
		};
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public ObjectSet<Map.Entry<T, Boolean>> entrySet() {
		return (ObjectSet)object2BooleanEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2BooleanMap) return object2BooleanEntrySet().containsAll(((Object2BooleanMap<T>)o).object2BooleanEntrySet());
			return object2BooleanEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2BooleanMap.Entry<T>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2BooleanMap.Entry<T> {
		protected T key;
		protected boolean value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
			return key;
		}

		@Override
		public boolean getBooleanValue() {
			return value;
		}

		@Override
		public boolean setValue(boolean value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2BooleanMap.Entry) {
					Object2BooleanMap.Entry<T> entry = (Object2BooleanMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && value == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Boolean && Objects.equals(this.key, key) && this.value == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Boolean.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Boolean.toString(value);
		}
	}
}
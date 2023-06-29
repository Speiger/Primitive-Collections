package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortBooleanConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.shorts.functions.function.ShortBooleanUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2BooleanMaps;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractShort2BooleanMap extends AbstractMap<Short, Boolean> implements Short2BooleanMap
{
	protected boolean defaultReturnValue = false;
	
	@Override
	public boolean getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2BooleanMap setDefaultReturnValue(boolean v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Short2BooleanMap.Entry> getFastIterable(Short2BooleanMap map) {
		return Short2BooleanMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Short2BooleanMap.Entry> getFastIterator(Short2BooleanMap map) {
		return Short2BooleanMaps.fastIterator(map);
	}
	
	@Override
	public Short2BooleanMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Boolean put(Short key, Boolean value) {
		return Boolean.valueOf(put(key.shortValue(), value.booleanValue()));
	}
	
	@Override
	public void putAll(Short2BooleanMap m) {
		for(ObjectIterator<Short2BooleanMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Short2BooleanMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getBooleanValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Boolean> m)
	{
		if(m instanceof Short2BooleanMap) putAll((Short2BooleanMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Short2BooleanMap m) {
		for(Short2BooleanMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getBooleanValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(BooleanIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextBoolean() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, boolean oldValue, boolean newValue) {
		boolean curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public boolean replace(short key, boolean value) {
		boolean curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBooleans(Short2BooleanMap m) {
		for(Short2BooleanMap.Entry entry : getFastIterable(m))
			replace(entry.getShortKey(), entry.getBooleanValue());
	}
	
	@Override
	public void replaceBooleans(ShortBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2BooleanMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsBoolean(entry.getShortKey(), entry.getBooleanValue()));
		}
	}

	@Override
	public boolean computeBoolean(short key, ShortBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(short key, ShortPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(short key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean computeBooleanIfPresent(short key, ShortBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean computeBooleanNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value = get(key);
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
	public boolean computeBooleanIfAbsentNonDefault(short key, ShortPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean supplyBooleanIfAbsentNonDefault(short key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(short key, ShortBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
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
	public boolean mergeBoolean(short key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean oldValue = get(key);
		boolean newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Short2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2BooleanMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			boolean oldValue = get(key);
			boolean newValue = oldValue == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(oldValue, entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Boolean.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public boolean getOrDefault(short key, boolean defaultValue) {
		boolean value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Boolean remove(Object key) {
		return key instanceof Short ? Boolean.valueOf(remove(((Short)key).shortValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ShortBooleanConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2BooleanMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getBooleanValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return AbstractShort2BooleanMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2BooleanMap.Entry> iter = getFastIterator(AbstractShort2BooleanMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public short nextShort() {
						return iter.next().getShortKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractShort2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2BooleanMap.this.clear();
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
				return AbstractShort2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2BooleanMap.this.clear();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Short2BooleanMap.Entry> iter = getFastIterator(AbstractShort2BooleanMap.this);
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
	public ObjectSet<Map.Entry<Short, Boolean>> entrySet() {
		return (ObjectSet)short2BooleanEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2BooleanMap) return short2BooleanEntrySet().containsAll(((Short2BooleanMap)o).short2BooleanEntrySet());
			return short2BooleanEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2BooleanMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2BooleanMap.Entry {
		protected short key;
		protected boolean value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Short key, Boolean value) {
			this.key = key.shortValue();
			this.value = value.booleanValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2BooleanMap.Entry) {
					Short2BooleanMap.Entry entry = (Short2BooleanMap.Entry)obj;
					return key == entry.getShortKey() && value == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Boolean && this.key == ((Short)key).shortValue() && this.value == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Boolean.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Boolean.toString(value);
		}
	}
}
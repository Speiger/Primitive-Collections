package speiger.src.collections.booleans.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.consumer.BooleanShortConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2ShortFunction;
import speiger.src.collections.booleans.functions.function.BooleanShortUnaryOperator;
import speiger.src.collections.booleans.maps.interfaces.Boolean2ShortMap;
import speiger.src.collections.booleans.sets.AbstractBooleanSet;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.booleans.utils.maps.Boolean2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractBoolean2ShortMap extends AbstractMap<Boolean, Short> implements Boolean2ShortMap
{
	protected short defaultReturnValue = (short)0;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractBoolean2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Boolean2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Boolean key, Short value) {
		return Short.valueOf(put(key.booleanValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Boolean2ShortMap m) {
		for(Boolean2ShortMap.Entry entry : Boolean2ShortMaps.fastIterable(m))
			addTo(entry.getBooleanKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Boolean2ShortMap m) {
		for(ObjectIterator<Boolean2ShortMap.Entry> iter = Boolean2ShortMaps.fastIterator(m);iter.hasNext();) {
			Boolean2ShortMap.Entry entry = iter.next();
			put(entry.getBooleanKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Boolean, ? extends Short> m)
	{
		if(m instanceof Boolean2ShortMap) putAll((Boolean2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(boolean[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Boolean[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Boolean2ShortMap m) {
		for(Boolean2ShortMap.Entry entry : Boolean2ShortMaps.fastIterable(m))
			putIfAbsent(entry.getBooleanKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(boolean key) {
		for(BooleanIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextBoolean() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(boolean key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(boolean key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Boolean2ShortMap m) {
		for(Boolean2ShortMap.Entry entry : Boolean2ShortMaps.fastIterable(m))
			replace(entry.getBooleanKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(BooleanShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Boolean2ShortMap.Entry> iter = Boolean2ShortMaps.fastIterator(this);iter.hasNext();) {
			Boolean2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getBooleanKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(boolean key, BooleanShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value = get(key);
		short newValue = mappingFunction.applyAsShort(key, value);
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
	public short computeShortIfAbsent(boolean key, Boolean2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = mappingFunction.get(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short supplyShortIfAbsent(boolean key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		short value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = valueProvider.getShort();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short computeShortIfPresent(boolean key, BooleanShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}

	@Override
	public short mergeShort(boolean key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Boolean2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Boolean2ShortMap.Entry entry : Boolean2ShortMaps.fastIterable(m)) {
			boolean key = entry.getBooleanKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Boolean ? getOrDefault(((Boolean)key).booleanValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(boolean key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(BooleanShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Boolean2ShortMap.Entry> iter = Boolean2ShortMaps.fastIterator(this);iter.hasNext();) {
			Boolean2ShortMap.Entry entry = iter.next();
			action.accept(entry.getBooleanKey(), entry.getShortValue());
		}
	}

	@Override
	public BooleanSet keySet() {
		return new AbstractBooleanSet() {
			@Override
			public boolean remove(boolean o) {
				return AbstractBoolean2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Boolean2ShortMap.Entry> iter = Boolean2ShortMaps.fastIterator(AbstractBoolean2ShortMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public boolean nextBoolean() {
						return iter.next().getBooleanKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractBoolean2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2ShortMap.this.clear();
			}
		};
	}

	@Override
	public ShortCollection values() {
		return new AbstractShortCollection() {
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractBoolean2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Boolean2ShortMap.Entry> iter = Boolean2ShortMaps.fastIterator(AbstractBoolean2ShortMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public short nextShort() {
						return iter.next().getShortValue();
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
	public ObjectSet<Map.Entry<Boolean, Short>> entrySet() {
		return (ObjectSet)boolean2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Boolean2ShortMap) return boolean2ShortEntrySet().containsAll(((Boolean2ShortMap)o).boolean2ShortEntrySet());
			return boolean2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Boolean2ShortMap.Entry> iter = Boolean2ShortMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Boolean2ShortMap.Entry {
		protected boolean key;
		protected short value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Boolean key, Short value) {
			this.key = key.booleanValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(boolean key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(boolean key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean getBooleanKey() {
			return key;
		}

		@Override
		public short getShortValue() {
			return value;
		}

		@Override
		public short setValue(short value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Boolean2ShortMap.Entry) {
					Boolean2ShortMap.Entry entry = (Boolean2ShortMap.Entry)obj;
					return key == entry.getBooleanKey() && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Boolean && value instanceof Short && this.key == ((Boolean)key).booleanValue() && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Boolean.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Boolean.toString(key) + "=" + Short.toString(value);
		}
	}
}
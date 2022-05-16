package speiger.src.collections.booleans.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.consumer.BooleanBooleanConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.maps.interfaces.Boolean2BooleanMap;
import speiger.src.collections.booleans.sets.AbstractBooleanSet;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.booleans.utils.maps.Boolean2BooleanMaps;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractBoolean2BooleanMap extends AbstractMap<Boolean, Boolean> implements Boolean2BooleanMap
{
	protected boolean defaultReturnValue = false;
	
	@Override
	public boolean getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractBoolean2BooleanMap setDefaultReturnValue(boolean v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Boolean2BooleanMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Boolean put(Boolean key, Boolean value) {
		return Boolean.valueOf(put(key.booleanValue(), value.booleanValue()));
	}
	
	@Override
	public void putAll(Boolean2BooleanMap m) {
		for(ObjectIterator<Boolean2BooleanMap.Entry> iter = Boolean2BooleanMaps.fastIterator(m);iter.hasNext();) {
			Boolean2BooleanMap.Entry entry = iter.next();
			put(entry.getBooleanKey(), entry.getBooleanValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Boolean, ? extends Boolean> m)
	{
		if(m instanceof Boolean2BooleanMap) putAll((Boolean2BooleanMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(boolean[] keys, boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Boolean[] keys, Boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Boolean2BooleanMap m) {
		for(Boolean2BooleanMap.Entry entry : Boolean2BooleanMaps.fastIterable(m))
			putIfAbsent(entry.getBooleanKey(), entry.getBooleanValue());
	}
	
	
	@Override
	public boolean containsKey(boolean key) {
		for(BooleanIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextBoolean() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(BooleanIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextBoolean() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(boolean key, boolean oldValue, boolean newValue) {
		boolean curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public boolean replace(boolean key, boolean value) {
		boolean curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBooleans(Boolean2BooleanMap m) {
		for(Boolean2BooleanMap.Entry entry : Boolean2BooleanMaps.fastIterable(m))
			replace(entry.getBooleanKey(), entry.getBooleanValue());
	}
	
	@Override
	public void replaceBooleans(BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Boolean2BooleanMap.Entry> iter = Boolean2BooleanMaps.fastIterator(this);iter.hasNext();) {
			Boolean2BooleanMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsBoolean(entry.getBooleanKey(), entry.getBooleanValue()));
		}
	}

	@Override
	public boolean computeBoolean(boolean key, BooleanBooleanUnaryOperator mappingFunction) {
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
	public boolean computeBooleanIfAbsent(boolean key, Boolean2BooleanFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = mappingFunction.get(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(boolean key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = valueProvider.getBoolean();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean computeBooleanIfPresent(boolean key, BooleanBooleanUnaryOperator mappingFunction) {
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
	public boolean mergeBoolean(boolean key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean oldValue = get(key);
		boolean newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Boolean2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Boolean2BooleanMap.Entry entry : Boolean2BooleanMaps.fastIterable(m)) {
			boolean key = entry.getBooleanKey();
			boolean oldValue = get(key);
			boolean newValue = oldValue == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(oldValue, entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Boolean.valueOf(key instanceof Boolean ? getOrDefault(((Boolean)key).booleanValue(), defaultValue.booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public boolean getOrDefault(boolean key, boolean defaultValue) {
		boolean value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(BooleanBooleanConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Boolean2BooleanMap.Entry> iter = Boolean2BooleanMaps.fastIterator(this);iter.hasNext();) {
			Boolean2BooleanMap.Entry entry = iter.next();
			action.accept(entry.getBooleanKey(), entry.getBooleanValue());
		}
	}

	@Override
	public BooleanSet keySet() {
		return new AbstractBooleanSet() {
			@Override
			public boolean remove(boolean o) {
				return AbstractBoolean2BooleanMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Boolean2BooleanMap.Entry> iter = Boolean2BooleanMaps.fastIterator(AbstractBoolean2BooleanMap.this);
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
				return AbstractBoolean2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2BooleanMap.this.clear();
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
				return AbstractBoolean2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2BooleanMap.this.clear();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Boolean2BooleanMap.Entry> iter = Boolean2BooleanMaps.fastIterator(AbstractBoolean2BooleanMap.this);
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
	public ObjectSet<Map.Entry<Boolean, Boolean>> entrySet() {
		return (ObjectSet)boolean2BooleanEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Boolean2BooleanMap) return boolean2BooleanEntrySet().containsAll(((Boolean2BooleanMap)o).boolean2BooleanEntrySet());
			return boolean2BooleanEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Boolean2BooleanMap.Entry> iter = Boolean2BooleanMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Boolean2BooleanMap.Entry {
		protected boolean key;
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
		public BasicEntry(Boolean key, Boolean value) {
			this.key = key.booleanValue();
			this.value = value.booleanValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(boolean key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(boolean key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean getBooleanKey() {
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
				if(obj instanceof Boolean2BooleanMap.Entry) {
					Boolean2BooleanMap.Entry entry = (Boolean2BooleanMap.Entry)obj;
					return key == entry.getBooleanKey() && value == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Boolean && value instanceof Boolean && this.key == ((Boolean)key).booleanValue() && this.value == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Boolean.hashCode(key) ^ Boolean.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Boolean.toString(key) + "=" + Boolean.toString(value);
		}
	}
}
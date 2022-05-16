package speiger.src.collections.booleans.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.consumer.BooleanObjectConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2ObjectFunction;
import speiger.src.collections.booleans.functions.function.BooleanObjectUnaryOperator;
import speiger.src.collections.booleans.maps.interfaces.Boolean2ObjectMap;
import speiger.src.collections.booleans.sets.AbstractBooleanSet;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.booleans.utils.maps.Boolean2ObjectMaps;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <V> the type of elements maintained by this Collection
 */
public abstract class AbstractBoolean2ObjectMap<V> extends AbstractMap<Boolean, V> implements Boolean2ObjectMap<V>
{
	protected V defaultReturnValue = null;
	
	@Override
	public V getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractBoolean2ObjectMap<V> setDefaultReturnValue(V v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Boolean2ObjectMap<V> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public V put(Boolean key, V value) {
		return put(key.booleanValue(), value);
	}
	
	@Override
	public void putAll(Boolean2ObjectMap<V> m) {
		for(ObjectIterator<Boolean2ObjectMap.Entry<V>> iter = Boolean2ObjectMaps.fastIterator(m);iter.hasNext();) {
			Boolean2ObjectMap.Entry<V> entry = iter.next();
			put(entry.getBooleanKey(), entry.getValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Boolean, ? extends V> m)
	{
		if(m instanceof Boolean2ObjectMap) putAll((Boolean2ObjectMap<V>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(boolean[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Boolean[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Boolean2ObjectMap<V> m) {
		for(Boolean2ObjectMap.Entry<V> entry : Boolean2ObjectMaps.fastIterable(m))
			putIfAbsent(entry.getBooleanKey(), entry.getValue());
	}
	
	
	@Override
	public boolean containsKey(boolean key) {
		for(BooleanIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextBoolean() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for(ObjectIterator<V> iter = values().iterator();iter.hasNext();)
			if(Objects.equals(value, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean replace(boolean key, V oldValue, V newValue) {
		V curValue = get(key);
		if (!Objects.equals(curValue, oldValue) || (Objects.equals(curValue, getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public V replace(boolean key, V value) {
		V curValue;
		if (!Objects.equals((curValue = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceObjects(Boolean2ObjectMap<V> m) {
		for(Boolean2ObjectMap.Entry<V> entry : Boolean2ObjectMaps.fastIterable(m))
			replace(entry.getBooleanKey(), entry.getValue());
	}
	
	@Override
	public void replaceObjects(BooleanObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Boolean2ObjectMap.Entry<V>> iter = Boolean2ObjectMaps.fastIterator(this);iter.hasNext();) {
			Boolean2ObjectMap.Entry<V> entry = iter.next();
			entry.setValue(mappingFunction.apply(entry.getBooleanKey(), entry.getValue()));
		}
	}

	@Override
	public V compute(boolean key, BooleanObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value = get(key);
		V newValue = mappingFunction.apply(key, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			if(!Objects.equals(value, getDefaultReturnValue()) || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public V computeIfAbsent(boolean key, Boolean2ObjectFunction<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			V newValue = mappingFunction.get(key);
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public V supplyIfAbsent(boolean key, ObjectSupplier<V> valueProvider) {
		Objects.requireNonNull(valueProvider);
		V value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			V newValue = valueProvider.get();
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public V computeIfPresent(boolean key, BooleanObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value;
		if(!Objects.equals((value = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			V newValue = mappingFunction.apply(key, value);
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}

	@Override
	public V merge(boolean key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V oldValue = get(key);
		V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? value : mappingFunction.apply(oldValue, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAll(Boolean2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Boolean2ObjectMap.Entry<V> entry : Boolean2ObjectMaps.fastIterable(m)) {
			boolean key = entry.getBooleanKey();
			V oldValue = get(key);
			V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(oldValue, entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public V get(Object key) {
		return key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return key instanceof Boolean ? getOrDefault(((Boolean)key).booleanValue(), defaultValue) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(boolean key, V defaultValue) {
		V value = get(key);
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(BooleanObjectConsumer<V> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Boolean2ObjectMap.Entry<V>> iter = Boolean2ObjectMaps.fastIterator(this);iter.hasNext();) {
			Boolean2ObjectMap.Entry<V> entry = iter.next();
			action.accept(entry.getBooleanKey(), entry.getValue());
		}
	}

	@Override
	public BooleanSet keySet() {
		return new AbstractBooleanSet() {
			@Override
			public boolean remove(boolean o) {
				return !Objects.equals(AbstractBoolean2ObjectMap.this.remove(o), getDefaultReturnValue());
			}
			
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Boolean2ObjectMap.Entry<V>> iter = Boolean2ObjectMaps.fastIterator(AbstractBoolean2ObjectMap.this);
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
				return AbstractBoolean2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2ObjectMap.this.clear();
			}
		};
	}

	@Override
	public ObjectCollection<V> values() {
		return new AbstractObjectCollection<V>() {
			@Override
			public boolean add(V o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractBoolean2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2ObjectMap.this.clear();
			}
			
			@Override
			public ObjectIterator<V> iterator() {
				return new ObjectIterator<V>() {
					ObjectIterator<Boolean2ObjectMap.Entry<V>> iter = Boolean2ObjectMaps.fastIterator(AbstractBoolean2ObjectMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public V next() {
						return iter.next().getValue();
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
	public ObjectSet<Map.Entry<Boolean, V>> entrySet() {
		return (ObjectSet)boolean2ObjectEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Boolean2ObjectMap) return boolean2ObjectEntrySet().containsAll(((Boolean2ObjectMap<V>)o).boolean2ObjectEntrySet());
			return boolean2ObjectEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Boolean2ObjectMap.Entry<V>> iter = Boolean2ObjectMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class BasicEntry<V> implements Boolean2ObjectMap.Entry<V> {
		protected boolean key;
		protected V value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Boolean key, V value) {
			this.key = key.booleanValue();
			this.value = value;
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(boolean key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(boolean key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean getBooleanKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Boolean2ObjectMap.Entry) {
					Boolean2ObjectMap.Entry<V> entry = (Boolean2ObjectMap.Entry<V>)obj;
					return key == entry.getBooleanKey() && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Boolean && this.key == ((Boolean)key).booleanValue() && Objects.equals(this.value, value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Boolean.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Boolean.toString(key) + "=" + Objects.toString(value);
		}
	}
}
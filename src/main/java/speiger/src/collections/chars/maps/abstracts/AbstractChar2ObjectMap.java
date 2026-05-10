package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharObjectConsumer;
import speiger.src.collections.chars.functions.function.CharFunction;
import speiger.src.collections.chars.functions.function.CharObjectUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectOrderedMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.objects.collections.ObjectOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2ObjectMaps;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <V> the keyType of elements maintained by this Collection
 */
public abstract class AbstractChar2ObjectMap<V> extends AbstractMap<Character, V> implements Char2ObjectMap<V>
{
	protected V defaultReturnValue = null;
	
	@Override
	public V getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2ObjectMap<V> setDefaultReturnValue(V v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Char2ObjectMap.Entry<V>> getFastIterable(Char2ObjectMap<V> map) {
		return Char2ObjectMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Char2ObjectMap.Entry<V>> getFastIterator(Char2ObjectMap<V> map) {
		return Char2ObjectMaps.fastIterator(map);
	}
	
	@Override
	public Char2ObjectMap<V> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public V put(Character key, V value) {
		return put(key.charValue(), value);
	}
	
	@Override
	public void putAll(Char2ObjectMap<V> m) {
		for(ObjectIterator<Char2ObjectMap.Entry<V>> iter = getFastIterator(m);iter.hasNext();) {
			Char2ObjectMap.Entry<V> entry = iter.next();
			put(entry.getCharKey(), entry.getValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Character, ? extends V> m)
	{
		if(m instanceof Char2ObjectMap) putAll((Char2ObjectMap<V>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(char[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Character[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].charValue(), values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Char2ObjectMap<V> m) {
		for(Char2ObjectMap.Entry<V> entry : getFastIterable(m))
			putIfAbsent(entry.getCharKey(), entry.getValue());
	}
	
	
	@Override
	public boolean containsKey(char key) {
		for(CharIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextChar() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for(ObjectIterator<V> iter = values().iterator();iter.hasNext();)
			if(Objects.equals(value, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean replace(char key, V oldValue, V newValue) {
		V curValue = get(key);
		if (!Objects.equals(curValue, oldValue) || (Objects.equals(curValue, getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public V replace(char key, V value) {
		V curValue;
		if (!Objects.equals((curValue = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceObjects(Char2ObjectMap<V> m) {
		for(Char2ObjectMap.Entry<V> entry : getFastIterable(m))
			replace(entry.getCharKey(), entry.getValue());
	}
	
	@Override
	public void replaceObjects(CharObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Char2ObjectMap.Entry<V> entry = iter.next();
			entry.setValue(mappingFunction.apply(entry.getCharKey(), entry.getValue()));
		}
	}

	@Override
	public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) {
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
	public V computeIfAbsent(char key, CharFunction<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			V newValue = mappingFunction.apply(key);
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) {
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
	public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) {
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
	public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V oldValue = get(key);
		V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? value : mappingFunction.apply(oldValue, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2ObjectMap.Entry<V> entry : getFastIterable(m)) {
			char key = entry.getCharKey();
			V oldValue = get(key);
			V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(oldValue, entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public V get(Object key) {
		return key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return key instanceof Character ? getOrDefault(((Character)key).charValue(), defaultValue) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(char key, V defaultValue) {
		V value = get(key);
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public V remove(Object key) {
		return key instanceof Character ? remove(((Character)key).charValue()) : getDefaultReturnValue();
	}
	
	@Override
	public void forEach(CharObjectConsumer<V> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Char2ObjectMap.Entry<V> entry = iter.next();
			action.accept(entry.getCharKey(), entry.getValue());
		}
	}

	@Override
	public CharSet keySet() {
		return new AbstractCharSet() {
			@Override
			public boolean remove(char o) {
				return !Objects.equals(AbstractChar2ObjectMap.this.remove(o), getDefaultReturnValue());
			}
			
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2ObjectMap.Entry<V>> iter = getFastIterator(AbstractChar2ObjectMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public char nextChar() {
						return iter.next().getCharKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractChar2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2ObjectMap.this.clear();
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
				return AbstractChar2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2ObjectMap.this.clear();
			}
			
			@Override
			public ObjectIterator<V> iterator() {
				return new ObjectIterator<V>() {
					ObjectIterator<Char2ObjectMap.Entry<V>> iter = getFastIterator(AbstractChar2ObjectMap.this);
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
	public ObjectSet<Map.Entry<Character, V>> entrySet() {
		return (ObjectSet)char2ObjectEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Char2ObjectMap) return char2ObjectEntrySet().containsAll(((Char2ObjectMap<V>)o).char2ObjectEntrySet());
			return char2ObjectEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Char2ObjectMap.Entry<V>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedChar2ObjectOrderedMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectOrderedMap<V> {
		Char2ObjectOrderedMap<V> map;
		
		public ReversedChar2ObjectOrderedMap(Char2ObjectOrderedMap<V> map) {
			this.map = map;
		}
		@Override
		public AbstractChar2ObjectMap<V> setDefaultReturnValue(V v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public V getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Char2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public V put(char key, V value) { return map.put(key, value); }
		@Override
		public V putIfAbsent(char key, V value) { return map.putIfAbsent(key, value); }
		@Override
		public V remove(char key) { return map.remove(key); }
		@Override
		public boolean remove(char key, V value) { return map.remove(key, value); }
		@Override
		public V removeOrDefault(char key, V defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(char key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(Object value) { return map.containsValue(value); }
		@Override
		public boolean replace(char key, V oldValue, V newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public V replace(char key, V value) { return map.replace(key, value); }
		@Override
		public void replaceObjects(Char2ObjectMap<V> m) { map.replaceObjects(m); }
		@Override
		public void replaceObjects(CharObjectUnaryOperator<V> mappingFunction) { map.replaceObjects(mappingFunction); }
		@Override
		public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) { return map.compute(key, mappingFunction); }
		@Override
		public V computeIfAbsent(char key, CharFunction<V> mappingFunction) { return map.computeIfAbsent(key, mappingFunction); }
		@Override
		public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) { return map.supplyIfAbsent(key, valueProvider); }
		@Override
		public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) { return map.computeIfPresent(key, mappingFunction); }
		@Override
		public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { return map.merge(key, value, mappingFunction); }
		@Override
		public V getOrDefault(char key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public V get(char key) { return map.get(key); }
		@Override
		public V putAndMoveToFirst(char key, V value) { return map.putAndMoveToLast(key, value); }
		@Override
		public V putAndMoveToLast(char key, V value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public V putFirst(char key, V value) { return map.putLast(key, value); }
		@Override
		public V putLast(char key, V value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(char key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(char key) { return map.moveToFirst(key); }
		@Override
		public V getAndMoveToFirst(char key) { return map.getAndMoveToLast(key); }
		@Override
		public V getAndMoveToLast(char key) { return map.getAndMoveToFirst(key); }
		@Override
		public char firstCharKey() { return map.lastCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollLastCharKey(); }
		@Override
		public char lastCharKey() { return map.firstCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollFirstCharKey(); }
		@Override
		public V firstValue() { return map.lastValue(); }
		@Override
		public V lastValue() { return map.firstValue(); }
		@Override
		public Char2ObjectMap.Entry<V> firstEntry() { return map.lastEntry(); }
		@Override
		public Char2ObjectMap.Entry<V> lastEntry() { return map.firstEntry(); }
		@Override
		public Char2ObjectMap.Entry<V> pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Char2ObjectMap.Entry<V> pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.char2ObjectEntrySet()); }
		@Override
		public CharOrderedSet keySet() { return new AbstractCharSet.ReversedCharOrderedSet(map.keySet()); }
		@Override
		public ObjectOrderedCollection<V> values() { return map.values().reversed(); }
		@Override
		public Char2ObjectOrderedMap<V> reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<V> implements Char2ObjectMap.Entry<V> {
		protected char key;
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
		public BasicEntry(Character key, V value) {
			this.key = key.charValue();
			this.value = value;
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(char key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(char key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char getCharKey() {
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
				if(obj instanceof Char2ObjectMap.Entry) {
					Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>)obj;
					return key == entry.getCharKey() && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && this.key == ((Character)key).charValue() && Objects.equals(this.value, value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Objects.toString(value);
		}
	}
}
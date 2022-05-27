package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2ObjectMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ObjectSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleChar2ObjectMapTestGenerator<V, E extends Char2ObjectMap<V>>
{
	BiFunction<char[], V[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	V[] values;
	
	public SimpleChar2ObjectMapTestGenerator(BiFunction<char[], V[], E> mapper) {
		this.mapper = mapper;
	}
	
	public void setValues(V... values) {
		this.values = values;
	}
	
	public ObjectSamples<Char2ObjectMap.Entry<V>> getSamples() {
		return new ObjectSamples<Char2ObjectMap.Entry<V>>(
			new AbstractChar2ObjectMap.BasicEntry<>(keys[0], values[0]),
			new AbstractChar2ObjectMap.BasicEntry<>(keys[1], values[1]),
			new AbstractChar2ObjectMap.BasicEntry<>(keys[2], values[2]),
			new AbstractChar2ObjectMap.BasicEntry<>(keys[3], values[3]),
			new AbstractChar2ObjectMap.BasicEntry<>(keys[4], values[4])
		);
	}
	
	public E create(Char2ObjectMap.Entry<V>... elements) {
		char[] keys = new char[elements.length];
		V[] values = (V[])ObjectArrays.newArray(getSamples().e0().getValue().getClass(), elements.length);
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, V>> order(List<Map.Entry<Character, V>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2ObjectMap.Entry<V>> order(ObjectList<Char2ObjectMap.Entry<V>> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps<V> extends SimpleChar2ObjectMapTestGenerator<V, Char2ObjectMap<V>> implements TestChar2ObjectMapGenerator<V>
	{
		public Maps(BiFunction<char[], V[], Char2ObjectMap<V>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps<V> extends SimpleChar2ObjectMapTestGenerator<V, Char2ObjectSortedMap<V>> implements TestChar2ObjectSortedMapGenerator<V>
	{
		public SortedMaps(BiFunction<char[], V[], Char2ObjectSortedMap<V>> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, V>> order(List<Map.Entry<Character, V>> insertionOrder) {
			insertionOrder.sort(DerivedChar2ObjectMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2ObjectMap.Entry<V>> order(ObjectList<Char2ObjectMap.Entry<V>> insertionOrder) {
			insertionOrder.sort(DerivedChar2ObjectMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2ObjectMap.Entry<V> belowSamplesLesser() { return new AbstractChar2ObjectMap.BasicEntry<>(keys[5], values[5]); }
		@Override
		public Char2ObjectMap.Entry<V> belowSamplesGreater() { return new AbstractChar2ObjectMap.BasicEntry<>(keys[6], values[6]); }
		@Override
		public Char2ObjectMap.Entry<V> aboveSamplesLesser() { return new AbstractChar2ObjectMap.BasicEntry<>(keys[7], values[7]); }
		@Override
		public Char2ObjectMap.Entry<V> aboveSamplesGreater() { return new AbstractChar2ObjectMap.BasicEntry<>(keys[8], values[8]); }
	}
}
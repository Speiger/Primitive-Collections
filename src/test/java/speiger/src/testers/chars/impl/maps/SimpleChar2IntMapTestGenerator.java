package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2IntMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleChar2IntMapTestGenerator<E extends Char2IntMap>
{
	BiFunction<char[], int[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleChar2IntMapTestGenerator(BiFunction<char[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2IntMap.Entry> getSamples() {
		return new ObjectSamples<Char2IntMap.Entry>(
			new AbstractChar2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2IntMap.Entry... elements) {
		char[] keys = new char[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Integer>> order(List<Map.Entry<Character, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2IntMap.Entry> order(ObjectList<Char2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2IntMapTestGenerator<Char2IntMap> implements TestChar2IntMapGenerator
	{
		public Maps(BiFunction<char[], int[], Char2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2IntMapTestGenerator<Char2IntSortedMap> implements TestChar2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], int[], Char2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Integer>> order(List<Map.Entry<Character, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedChar2IntMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2IntMap.Entry> order(ObjectList<Char2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2IntMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2IntMap.Entry belowSamplesLesser() { return new AbstractChar2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2IntMap.Entry belowSamplesGreater() { return new AbstractChar2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2IntMap.Entry aboveSamplesLesser() { return new AbstractChar2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2IntMap.Entry aboveSamplesGreater() { return new AbstractChar2IntMap.BasicEntry(keys[8], values[8]); }
	}
}
package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2LongMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleChar2LongMapTestGenerator<E extends Char2LongMap>
{
	BiFunction<char[], long[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleChar2LongMapTestGenerator(BiFunction<char[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2LongMap.Entry> getSamples() {
		return new ObjectSamples<Char2LongMap.Entry>(
			new AbstractChar2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2LongMap.Entry... elements) {
		char[] keys = new char[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Long>> order(List<Map.Entry<Character, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2LongMap.Entry> order(ObjectList<Char2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2LongMapTestGenerator<Char2LongMap> implements TestChar2LongMapGenerator
	{
		public Maps(BiFunction<char[], long[], Char2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2LongMapTestGenerator<Char2LongSortedMap> implements TestChar2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], long[], Char2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Long>> order(List<Map.Entry<Character, Long>> insertionOrder) {
			insertionOrder.sort(DerivedChar2LongMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2LongMap.Entry> order(ObjectList<Char2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2LongMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2LongMap.Entry belowSamplesLesser() { return new AbstractChar2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2LongMap.Entry belowSamplesGreater() { return new AbstractChar2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2LongMap.Entry aboveSamplesLesser() { return new AbstractChar2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2LongMap.Entry aboveSamplesGreater() { return new AbstractChar2LongMap.BasicEntry(keys[8], values[8]); }
	}
}
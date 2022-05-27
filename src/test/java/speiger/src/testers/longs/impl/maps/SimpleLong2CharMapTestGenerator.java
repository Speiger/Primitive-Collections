package speiger.src.testers.longs.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.collections.longs.maps.interfaces.Long2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.generators.maps.TestLong2CharMapGenerator;
import speiger.src.testers.longs.generators.maps.TestLong2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleLong2CharMapTestGenerator<E extends Long2CharMap>
{
	BiFunction<long[], char[], E> mapper;
	long[] keys = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleLong2CharMapTestGenerator(BiFunction<long[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Long2CharMap.Entry> getSamples() {
		return new ObjectSamples<Long2CharMap.Entry>(
			new AbstractLong2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractLong2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractLong2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractLong2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractLong2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Long2CharMap.Entry... elements) {
		long[] keys = new long[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getLongKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Long, Character>> order(List<Map.Entry<Long, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Long2CharMap.Entry> order(ObjectList<Long2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleLong2CharMapTestGenerator<Long2CharMap> implements TestLong2CharMapGenerator
	{
		public Maps(BiFunction<long[], char[], Long2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleLong2CharMapTestGenerator<Long2CharSortedMap> implements TestLong2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<long[], char[], Long2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Long, Character>> order(List<Map.Entry<Long, Character>> insertionOrder) {
			insertionOrder.sort(DerivedLong2CharMapGenerators.entryObjectComparator(Long::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Long2CharMap.Entry> order(ObjectList<Long2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedLong2CharMapGenerators.entryComparator(Long::compare));
			return insertionOrder;
		}
		
		@Override
		public Long2CharMap.Entry belowSamplesLesser() { return new AbstractLong2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Long2CharMap.Entry belowSamplesGreater() { return new AbstractLong2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Long2CharMap.Entry aboveSamplesLesser() { return new AbstractLong2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Long2CharMap.Entry aboveSamplesGreater() { return new AbstractLong2CharMap.BasicEntry(keys[8], values[8]); }
	}
}
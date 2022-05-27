package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2LongMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleInt2LongMapTestGenerator<E extends Int2LongMap>
{
	BiFunction<int[], long[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleInt2LongMapTestGenerator(BiFunction<int[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2LongMap.Entry> getSamples() {
		return new ObjectSamples<Int2LongMap.Entry>(
			new AbstractInt2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2LongMap.Entry... elements) {
		int[] keys = new int[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Long>> order(List<Map.Entry<Integer, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2LongMap.Entry> order(ObjectList<Int2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2LongMapTestGenerator<Int2LongMap> implements TestInt2LongMapGenerator
	{
		public Maps(BiFunction<int[], long[], Int2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2LongMapTestGenerator<Int2LongSortedMap> implements TestInt2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], long[], Int2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Long>> order(List<Map.Entry<Integer, Long>> insertionOrder) {
			insertionOrder.sort(DerivedInt2LongMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2LongMap.Entry> order(ObjectList<Int2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2LongMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2LongMap.Entry belowSamplesLesser() { return new AbstractInt2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2LongMap.Entry belowSamplesGreater() { return new AbstractInt2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2LongMap.Entry aboveSamplesLesser() { return new AbstractInt2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2LongMap.Entry aboveSamplesGreater() { return new AbstractInt2LongMap.BasicEntry(keys[8], values[8]); }
	}
}
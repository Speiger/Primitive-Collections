package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2IntMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleInt2IntMapTestGenerator<E extends Int2IntMap>
{
	BiFunction<int[], int[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleInt2IntMapTestGenerator(BiFunction<int[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2IntMap.Entry> getSamples() {
		return new ObjectSamples<Int2IntMap.Entry>(
			new AbstractInt2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2IntMap.Entry... elements) {
		int[] keys = new int[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Integer>> order(List<Map.Entry<Integer, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2IntMap.Entry> order(ObjectList<Int2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2IntMapTestGenerator<Int2IntMap> implements TestInt2IntMapGenerator
	{
		public Maps(BiFunction<int[], int[], Int2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2IntMapTestGenerator<Int2IntSortedMap> implements TestInt2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], int[], Int2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Integer>> order(List<Map.Entry<Integer, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedInt2IntMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2IntMap.Entry> order(ObjectList<Int2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2IntMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2IntMap.Entry belowSamplesLesser() { return new AbstractInt2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2IntMap.Entry belowSamplesGreater() { return new AbstractInt2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2IntMap.Entry aboveSamplesLesser() { return new AbstractInt2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2IntMap.Entry aboveSamplesGreater() { return new AbstractInt2IntMap.BasicEntry(keys[8], values[8]); }
	}
}
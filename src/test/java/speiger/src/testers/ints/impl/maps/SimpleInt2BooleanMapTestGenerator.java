package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2BooleanMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleInt2BooleanMapTestGenerator<E extends Int2BooleanMap>
{
	BiFunction<int[], boolean[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleInt2BooleanMapTestGenerator(BiFunction<int[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Int2BooleanMap.Entry>(
			new AbstractInt2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2BooleanMap.Entry... elements) {
		int[] keys = new int[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Boolean>> order(List<Map.Entry<Integer, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2BooleanMap.Entry> order(ObjectList<Int2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2BooleanMapTestGenerator<Int2BooleanMap> implements TestInt2BooleanMapGenerator
	{
		public Maps(BiFunction<int[], boolean[], Int2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2BooleanMapTestGenerator<Int2BooleanSortedMap> implements TestInt2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], boolean[], Int2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Boolean>> order(List<Map.Entry<Integer, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedInt2BooleanMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2BooleanMap.Entry> order(ObjectList<Int2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2BooleanMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2BooleanMap.Entry belowSamplesLesser() { return new AbstractInt2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2BooleanMap.Entry belowSamplesGreater() { return new AbstractInt2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2BooleanMap.Entry aboveSamplesLesser() { return new AbstractInt2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2BooleanMap.Entry aboveSamplesGreater() { return new AbstractInt2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}
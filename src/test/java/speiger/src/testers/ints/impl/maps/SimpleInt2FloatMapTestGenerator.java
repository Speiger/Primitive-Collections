package speiger.src.testers.ints.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.generators.maps.TestInt2FloatMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleInt2FloatMapTestGenerator<E extends Int2FloatMap>
{
	BiFunction<int[], float[], E> mapper;
	int[] keys = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleInt2FloatMapTestGenerator(BiFunction<int[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Int2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Int2FloatMap.Entry>(
			new AbstractInt2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractInt2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractInt2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractInt2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractInt2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Int2FloatMap.Entry... elements) {
		int[] keys = new int[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getIntKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Integer, Float>> order(List<Map.Entry<Integer, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Int2FloatMap.Entry> order(ObjectList<Int2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleInt2FloatMapTestGenerator<Int2FloatMap> implements TestInt2FloatMapGenerator
	{
		public Maps(BiFunction<int[], float[], Int2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleInt2FloatMapTestGenerator<Int2FloatSortedMap> implements TestInt2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<int[], float[], Int2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Integer, Float>> order(List<Map.Entry<Integer, Float>> insertionOrder) {
			insertionOrder.sort(DerivedInt2FloatMapGenerators.entryObjectComparator(Integer::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Int2FloatMap.Entry> order(ObjectList<Int2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedInt2FloatMapGenerators.entryComparator(Integer::compare));
			return insertionOrder;
		}
		
		@Override
		public Int2FloatMap.Entry belowSamplesLesser() { return new AbstractInt2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Int2FloatMap.Entry belowSamplesGreater() { return new AbstractInt2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Int2FloatMap.Entry aboveSamplesLesser() { return new AbstractInt2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Int2FloatMap.Entry aboveSamplesGreater() { return new AbstractInt2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}
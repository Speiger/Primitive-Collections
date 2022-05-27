package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntMap;
import speiger.src.collections.floats.maps.interfaces.Float2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2IntMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleFloat2IntMapTestGenerator<E extends Float2IntMap>
{
	BiFunction<float[], int[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleFloat2IntMapTestGenerator(BiFunction<float[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2IntMap.Entry> getSamples() {
		return new ObjectSamples<Float2IntMap.Entry>(
			new AbstractFloat2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2IntMap.Entry... elements) {
		float[] keys = new float[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Integer>> order(List<Map.Entry<Float, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2IntMap.Entry> order(ObjectList<Float2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2IntMapTestGenerator<Float2IntMap> implements TestFloat2IntMapGenerator
	{
		public Maps(BiFunction<float[], int[], Float2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2IntMapTestGenerator<Float2IntSortedMap> implements TestFloat2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], int[], Float2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Integer>> order(List<Map.Entry<Float, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2IntMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2IntMap.Entry> order(ObjectList<Float2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2IntMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2IntMap.Entry belowSamplesLesser() { return new AbstractFloat2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2IntMap.Entry belowSamplesGreater() { return new AbstractFloat2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2IntMap.Entry aboveSamplesLesser() { return new AbstractFloat2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2IntMap.Entry aboveSamplesGreater() { return new AbstractFloat2IntMap.BasicEntry(keys[8], values[8]); }
	}
}
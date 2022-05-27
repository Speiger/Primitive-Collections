package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2LongMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2LongSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleFloat2LongMapTestGenerator<E extends Float2LongMap>
{
	BiFunction<float[], long[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	long[] values = new long[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleFloat2LongMapTestGenerator(BiFunction<float[], long[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2LongMap.Entry> getSamples() {
		return new ObjectSamples<Float2LongMap.Entry>(
			new AbstractFloat2LongMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2LongMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2LongMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2LongMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2LongMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2LongMap.Entry... elements) {
		float[] keys = new float[elements.length];
		long[] values = new long[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getLongValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Long>> order(List<Map.Entry<Float, Long>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2LongMap.Entry> order(ObjectList<Float2LongMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2LongMapTestGenerator<Float2LongMap> implements TestFloat2LongMapGenerator
	{
		public Maps(BiFunction<float[], long[], Float2LongMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2LongMapTestGenerator<Float2LongSortedMap> implements TestFloat2LongSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], long[], Float2LongSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Long>> order(List<Map.Entry<Float, Long>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2LongMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2LongMap.Entry> order(ObjectList<Float2LongMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2LongMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2LongMap.Entry belowSamplesLesser() { return new AbstractFloat2LongMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2LongMap.Entry belowSamplesGreater() { return new AbstractFloat2LongMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2LongMap.Entry aboveSamplesLesser() { return new AbstractFloat2LongMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2LongMap.Entry aboveSamplesGreater() { return new AbstractFloat2LongMap.BasicEntry(keys[8], values[8]); }
	}
}
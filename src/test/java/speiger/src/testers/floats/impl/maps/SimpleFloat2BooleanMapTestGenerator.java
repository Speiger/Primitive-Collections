package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.collections.floats.maps.interfaces.Float2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2BooleanMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleFloat2BooleanMapTestGenerator<E extends Float2BooleanMap>
{
	BiFunction<float[], boolean[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleFloat2BooleanMapTestGenerator(BiFunction<float[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Float2BooleanMap.Entry>(
			new AbstractFloat2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2BooleanMap.Entry... elements) {
		float[] keys = new float[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Boolean>> order(List<Map.Entry<Float, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2BooleanMap.Entry> order(ObjectList<Float2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2BooleanMapTestGenerator<Float2BooleanMap> implements TestFloat2BooleanMapGenerator
	{
		public Maps(BiFunction<float[], boolean[], Float2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2BooleanMapTestGenerator<Float2BooleanSortedMap> implements TestFloat2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], boolean[], Float2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Boolean>> order(List<Map.Entry<Float, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2BooleanMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2BooleanMap.Entry> order(ObjectList<Float2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2BooleanMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2BooleanMap.Entry belowSamplesLesser() { return new AbstractFloat2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2BooleanMap.Entry belowSamplesGreater() { return new AbstractFloat2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2BooleanMap.Entry aboveSamplesLesser() { return new AbstractFloat2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2BooleanMap.Entry aboveSamplesGreater() { return new AbstractFloat2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}
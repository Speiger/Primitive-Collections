package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2FloatMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleFloat2FloatMapTestGenerator<E extends Float2FloatMap>
{
	BiFunction<float[], float[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleFloat2FloatMapTestGenerator(BiFunction<float[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Float2FloatMap.Entry>(
			new AbstractFloat2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2FloatMap.Entry... elements) {
		float[] keys = new float[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Float>> order(List<Map.Entry<Float, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2FloatMap.Entry> order(ObjectList<Float2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2FloatMapTestGenerator<Float2FloatMap> implements TestFloat2FloatMapGenerator
	{
		public Maps(BiFunction<float[], float[], Float2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2FloatMapTestGenerator<Float2FloatSortedMap> implements TestFloat2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], float[], Float2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Float>> order(List<Map.Entry<Float, Float>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2FloatMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2FloatMap.Entry> order(ObjectList<Float2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2FloatMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2FloatMap.Entry belowSamplesLesser() { return new AbstractFloat2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2FloatMap.Entry belowSamplesGreater() { return new AbstractFloat2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2FloatMap.Entry aboveSamplesLesser() { return new AbstractFloat2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2FloatMap.Entry aboveSamplesGreater() { return new AbstractFloat2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}
package speiger.src.testers.floats.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.floats.maps.abstracts.AbstractFloat2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleMap;
import speiger.src.collections.floats.maps.interfaces.Float2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.floats.generators.maps.TestFloat2DoubleMapGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleFloat2DoubleMapTestGenerator<E extends Float2DoubleMap>
{
	BiFunction<float[], double[], E> mapper;
	float[] keys = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleFloat2DoubleMapTestGenerator(BiFunction<float[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Float2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Float2DoubleMap.Entry>(
			new AbstractFloat2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractFloat2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractFloat2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractFloat2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractFloat2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Float2DoubleMap.Entry... elements) {
		float[] keys = new float[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getFloatKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Float, Double>> order(List<Map.Entry<Float, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Float2DoubleMap.Entry> order(ObjectList<Float2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleFloat2DoubleMapTestGenerator<Float2DoubleMap> implements TestFloat2DoubleMapGenerator
	{
		public Maps(BiFunction<float[], double[], Float2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleFloat2DoubleMapTestGenerator<Float2DoubleSortedMap> implements TestFloat2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<float[], double[], Float2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Float, Double>> order(List<Map.Entry<Float, Double>> insertionOrder) {
			insertionOrder.sort(DerivedFloat2DoubleMapGenerators.entryObjectComparator(Float::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Float2DoubleMap.Entry> order(ObjectList<Float2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedFloat2DoubleMapGenerators.entryComparator(Float::compare));
			return insertionOrder;
		}
		
		@Override
		public Float2DoubleMap.Entry belowSamplesLesser() { return new AbstractFloat2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Float2DoubleMap.Entry belowSamplesGreater() { return new AbstractFloat2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Float2DoubleMap.Entry aboveSamplesLesser() { return new AbstractFloat2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Float2DoubleMap.Entry aboveSamplesGreater() { return new AbstractFloat2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}
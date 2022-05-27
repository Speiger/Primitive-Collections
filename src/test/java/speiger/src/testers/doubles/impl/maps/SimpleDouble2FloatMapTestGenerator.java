package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatMap;
import speiger.src.collections.doubles.maps.interfaces.Double2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2FloatMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleDouble2FloatMapTestGenerator<E extends Double2FloatMap>
{
	BiFunction<double[], float[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleDouble2FloatMapTestGenerator(BiFunction<double[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Double2FloatMap.Entry>(
			new AbstractDouble2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2FloatMap.Entry... elements) {
		double[] keys = new double[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Float>> order(List<Map.Entry<Double, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2FloatMap.Entry> order(ObjectList<Double2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2FloatMapTestGenerator<Double2FloatMap> implements TestDouble2FloatMapGenerator
	{
		public Maps(BiFunction<double[], float[], Double2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2FloatMapTestGenerator<Double2FloatSortedMap> implements TestDouble2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], float[], Double2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Float>> order(List<Map.Entry<Double, Float>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2FloatMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2FloatMap.Entry> order(ObjectList<Double2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2FloatMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2FloatMap.Entry belowSamplesLesser() { return new AbstractDouble2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2FloatMap.Entry belowSamplesGreater() { return new AbstractDouble2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2FloatMap.Entry aboveSamplesLesser() { return new AbstractDouble2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2FloatMap.Entry aboveSamplesGreater() { return new AbstractDouble2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}
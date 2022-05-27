package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2DoubleMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleDouble2DoubleMapTestGenerator<E extends Double2DoubleMap>
{
	BiFunction<double[], double[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleDouble2DoubleMapTestGenerator(BiFunction<double[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Double2DoubleMap.Entry>(
			new AbstractDouble2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2DoubleMap.Entry... elements) {
		double[] keys = new double[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Double>> order(List<Map.Entry<Double, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2DoubleMap.Entry> order(ObjectList<Double2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2DoubleMapTestGenerator<Double2DoubleMap> implements TestDouble2DoubleMapGenerator
	{
		public Maps(BiFunction<double[], double[], Double2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2DoubleMapTestGenerator<Double2DoubleSortedMap> implements TestDouble2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], double[], Double2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Double>> order(List<Map.Entry<Double, Double>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2DoubleMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2DoubleMap.Entry> order(ObjectList<Double2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2DoubleMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2DoubleMap.Entry belowSamplesLesser() { return new AbstractDouble2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2DoubleMap.Entry belowSamplesGreater() { return new AbstractDouble2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2DoubleMap.Entry aboveSamplesLesser() { return new AbstractDouble2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2DoubleMap.Entry aboveSamplesGreater() { return new AbstractDouble2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}
package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2BooleanMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleDouble2BooleanMapTestGenerator<E extends Double2BooleanMap>
{
	BiFunction<double[], boolean[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleDouble2BooleanMapTestGenerator(BiFunction<double[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Double2BooleanMap.Entry>(
			new AbstractDouble2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2BooleanMap.Entry... elements) {
		double[] keys = new double[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Boolean>> order(List<Map.Entry<Double, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2BooleanMap.Entry> order(ObjectList<Double2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2BooleanMapTestGenerator<Double2BooleanMap> implements TestDouble2BooleanMapGenerator
	{
		public Maps(BiFunction<double[], boolean[], Double2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2BooleanMapTestGenerator<Double2BooleanSortedMap> implements TestDouble2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], boolean[], Double2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Boolean>> order(List<Map.Entry<Double, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2BooleanMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2BooleanMap.Entry> order(ObjectList<Double2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2BooleanMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2BooleanMap.Entry belowSamplesLesser() { return new AbstractDouble2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2BooleanMap.Entry belowSamplesGreater() { return new AbstractDouble2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2BooleanMap.Entry aboveSamplesLesser() { return new AbstractDouble2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2BooleanMap.Entry aboveSamplesGreater() { return new AbstractDouble2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}
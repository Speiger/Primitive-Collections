package speiger.src.testers.doubles.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.generators.maps.TestDouble2ShortMapGenerator;
import speiger.src.testers.doubles.generators.maps.TestDouble2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleDouble2ShortMapTestGenerator<E extends Double2ShortMap>
{
	BiFunction<double[], short[], E> mapper;
	double[] keys = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleDouble2ShortMapTestGenerator(BiFunction<double[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Double2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Double2ShortMap.Entry>(
			new AbstractDouble2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractDouble2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractDouble2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractDouble2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractDouble2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Double2ShortMap.Entry... elements) {
		double[] keys = new double[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getDoubleKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Double, Short>> order(List<Map.Entry<Double, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Double2ShortMap.Entry> order(ObjectList<Double2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleDouble2ShortMapTestGenerator<Double2ShortMap> implements TestDouble2ShortMapGenerator
	{
		public Maps(BiFunction<double[], short[], Double2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleDouble2ShortMapTestGenerator<Double2ShortSortedMap> implements TestDouble2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<double[], short[], Double2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Double, Short>> order(List<Map.Entry<Double, Short>> insertionOrder) {
			insertionOrder.sort(DerivedDouble2ShortMapGenerators.entryObjectComparator(Double::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Double2ShortMap.Entry> order(ObjectList<Double2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedDouble2ShortMapGenerators.entryComparator(Double::compare));
			return insertionOrder;
		}
		
		@Override
		public Double2ShortMap.Entry belowSamplesLesser() { return new AbstractDouble2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Double2ShortMap.Entry belowSamplesGreater() { return new AbstractDouble2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Double2ShortMap.Entry aboveSamplesLesser() { return new AbstractDouble2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Double2ShortMap.Entry aboveSamplesGreater() { return new AbstractDouble2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}
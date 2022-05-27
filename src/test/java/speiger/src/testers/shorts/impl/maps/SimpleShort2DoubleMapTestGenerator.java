package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2DoubleMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleShort2DoubleMapTestGenerator<E extends Short2DoubleMap>
{
	BiFunction<short[], double[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleShort2DoubleMapTestGenerator(BiFunction<short[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Short2DoubleMap.Entry>(
			new AbstractShort2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2DoubleMap.Entry... elements) {
		short[] keys = new short[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Double>> order(List<Map.Entry<Short, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2DoubleMap.Entry> order(ObjectList<Short2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2DoubleMapTestGenerator<Short2DoubleMap> implements TestShort2DoubleMapGenerator
	{
		public Maps(BiFunction<short[], double[], Short2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2DoubleMapTestGenerator<Short2DoubleSortedMap> implements TestShort2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], double[], Short2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Double>> order(List<Map.Entry<Short, Double>> insertionOrder) {
			insertionOrder.sort(DerivedShort2DoubleMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2DoubleMap.Entry> order(ObjectList<Short2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2DoubleMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2DoubleMap.Entry belowSamplesLesser() { return new AbstractShort2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2DoubleMap.Entry belowSamplesGreater() { return new AbstractShort2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2DoubleMap.Entry aboveSamplesLesser() { return new AbstractShort2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2DoubleMap.Entry aboveSamplesGreater() { return new AbstractShort2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}
package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2FloatMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleShort2FloatMapTestGenerator<E extends Short2FloatMap>
{
	BiFunction<short[], float[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleShort2FloatMapTestGenerator(BiFunction<short[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Short2FloatMap.Entry>(
			new AbstractShort2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2FloatMap.Entry... elements) {
		short[] keys = new short[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Float>> order(List<Map.Entry<Short, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2FloatMap.Entry> order(ObjectList<Short2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2FloatMapTestGenerator<Short2FloatMap> implements TestShort2FloatMapGenerator
	{
		public Maps(BiFunction<short[], float[], Short2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2FloatMapTestGenerator<Short2FloatSortedMap> implements TestShort2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], float[], Short2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Float>> order(List<Map.Entry<Short, Float>> insertionOrder) {
			insertionOrder.sort(DerivedShort2FloatMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2FloatMap.Entry> order(ObjectList<Short2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2FloatMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2FloatMap.Entry belowSamplesLesser() { return new AbstractShort2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2FloatMap.Entry belowSamplesGreater() { return new AbstractShort2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2FloatMap.Entry aboveSamplesLesser() { return new AbstractShort2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2FloatMap.Entry aboveSamplesGreater() { return new AbstractShort2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}
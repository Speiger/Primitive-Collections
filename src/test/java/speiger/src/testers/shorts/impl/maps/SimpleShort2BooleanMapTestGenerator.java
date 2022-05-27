package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2BooleanMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleShort2BooleanMapTestGenerator<E extends Short2BooleanMap>
{
	BiFunction<short[], boolean[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleShort2BooleanMapTestGenerator(BiFunction<short[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Short2BooleanMap.Entry>(
			new AbstractShort2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2BooleanMap.Entry... elements) {
		short[] keys = new short[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Boolean>> order(List<Map.Entry<Short, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2BooleanMap.Entry> order(ObjectList<Short2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2BooleanMapTestGenerator<Short2BooleanMap> implements TestShort2BooleanMapGenerator
	{
		public Maps(BiFunction<short[], boolean[], Short2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2BooleanMapTestGenerator<Short2BooleanSortedMap> implements TestShort2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], boolean[], Short2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Boolean>> order(List<Map.Entry<Short, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedShort2BooleanMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2BooleanMap.Entry> order(ObjectList<Short2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2BooleanMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2BooleanMap.Entry belowSamplesLesser() { return new AbstractShort2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2BooleanMap.Entry belowSamplesGreater() { return new AbstractShort2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2BooleanMap.Entry aboveSamplesLesser() { return new AbstractShort2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2BooleanMap.Entry aboveSamplesGreater() { return new AbstractShort2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}
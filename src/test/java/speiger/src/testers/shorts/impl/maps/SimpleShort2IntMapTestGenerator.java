package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2IntMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2IntSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleShort2IntMapTestGenerator<E extends Short2IntMap>
{
	BiFunction<short[], int[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	int[] values = new int[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleShort2IntMapTestGenerator(BiFunction<short[], int[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2IntMap.Entry> getSamples() {
		return new ObjectSamples<Short2IntMap.Entry>(
			new AbstractShort2IntMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2IntMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2IntMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2IntMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2IntMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2IntMap.Entry... elements) {
		short[] keys = new short[elements.length];
		int[] values = new int[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getIntValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Integer>> order(List<Map.Entry<Short, Integer>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2IntMap.Entry> order(ObjectList<Short2IntMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2IntMapTestGenerator<Short2IntMap> implements TestShort2IntMapGenerator
	{
		public Maps(BiFunction<short[], int[], Short2IntMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2IntMapTestGenerator<Short2IntSortedMap> implements TestShort2IntSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], int[], Short2IntSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Integer>> order(List<Map.Entry<Short, Integer>> insertionOrder) {
			insertionOrder.sort(DerivedShort2IntMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2IntMap.Entry> order(ObjectList<Short2IntMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2IntMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2IntMap.Entry belowSamplesLesser() { return new AbstractShort2IntMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2IntMap.Entry belowSamplesGreater() { return new AbstractShort2IntMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2IntMap.Entry aboveSamplesLesser() { return new AbstractShort2IntMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2IntMap.Entry aboveSamplesGreater() { return new AbstractShort2IntMap.BasicEntry(keys[8], values[8]); }
	}
}
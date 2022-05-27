package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2ShortMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleShort2ShortMapTestGenerator<E extends Short2ShortMap>
{
	BiFunction<short[], short[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleShort2ShortMapTestGenerator(BiFunction<short[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Short2ShortMap.Entry>(
			new AbstractShort2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2ShortMap.Entry... elements) {
		short[] keys = new short[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Short>> order(List<Map.Entry<Short, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2ShortMap.Entry> order(ObjectList<Short2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2ShortMapTestGenerator<Short2ShortMap> implements TestShort2ShortMapGenerator
	{
		public Maps(BiFunction<short[], short[], Short2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2ShortMapTestGenerator<Short2ShortSortedMap> implements TestShort2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], short[], Short2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Short>> order(List<Map.Entry<Short, Short>> insertionOrder) {
			insertionOrder.sort(DerivedShort2ShortMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2ShortMap.Entry> order(ObjectList<Short2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2ShortMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2ShortMap.Entry belowSamplesLesser() { return new AbstractShort2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2ShortMap.Entry belowSamplesGreater() { return new AbstractShort2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2ShortMap.Entry aboveSamplesLesser() { return new AbstractShort2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2ShortMap.Entry aboveSamplesGreater() { return new AbstractShort2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}
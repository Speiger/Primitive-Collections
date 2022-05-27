package speiger.src.testers.shorts.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.generators.maps.TestShort2CharMapGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleShort2CharMapTestGenerator<E extends Short2CharMap>
{
	BiFunction<short[], char[], E> mapper;
	short[] keys = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleShort2CharMapTestGenerator(BiFunction<short[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Short2CharMap.Entry> getSamples() {
		return new ObjectSamples<Short2CharMap.Entry>(
			new AbstractShort2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractShort2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractShort2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractShort2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractShort2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Short2CharMap.Entry... elements) {
		short[] keys = new short[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getShortKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Short, Character>> order(List<Map.Entry<Short, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Short2CharMap.Entry> order(ObjectList<Short2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleShort2CharMapTestGenerator<Short2CharMap> implements TestShort2CharMapGenerator
	{
		public Maps(BiFunction<short[], char[], Short2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleShort2CharMapTestGenerator<Short2CharSortedMap> implements TestShort2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<short[], char[], Short2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Short, Character>> order(List<Map.Entry<Short, Character>> insertionOrder) {
			insertionOrder.sort(DerivedShort2CharMapGenerators.entryObjectComparator(Short::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Short2CharMap.Entry> order(ObjectList<Short2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedShort2CharMapGenerators.entryComparator(Short::compare));
			return insertionOrder;
		}
		
		@Override
		public Short2CharMap.Entry belowSamplesLesser() { return new AbstractShort2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Short2CharMap.Entry belowSamplesGreater() { return new AbstractShort2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Short2CharMap.Entry aboveSamplesLesser() { return new AbstractShort2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Short2CharMap.Entry aboveSamplesGreater() { return new AbstractShort2CharMap.BasicEntry(keys[8], values[8]); }
	}
}
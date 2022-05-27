package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2ShortMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ShortSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleChar2ShortMapTestGenerator<E extends Char2ShortMap>
{
	BiFunction<char[], short[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	short[] values = new short[]{(short)0, (short)1, (short)2, (short)3, (short)4, (short)-2, (short)-1, (short)5, (short)6};
	
	public SimpleChar2ShortMapTestGenerator(BiFunction<char[], short[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2ShortMap.Entry> getSamples() {
		return new ObjectSamples<Char2ShortMap.Entry>(
			new AbstractChar2ShortMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2ShortMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2ShortMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2ShortMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2ShortMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2ShortMap.Entry... elements) {
		char[] keys = new char[elements.length];
		short[] values = new short[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getShortValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Short>> order(List<Map.Entry<Character, Short>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2ShortMap.Entry> order(ObjectList<Char2ShortMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2ShortMapTestGenerator<Char2ShortMap> implements TestChar2ShortMapGenerator
	{
		public Maps(BiFunction<char[], short[], Char2ShortMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2ShortMapTestGenerator<Char2ShortSortedMap> implements TestChar2ShortSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], short[], Char2ShortSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Short>> order(List<Map.Entry<Character, Short>> insertionOrder) {
			insertionOrder.sort(DerivedChar2ShortMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2ShortMap.Entry> order(ObjectList<Char2ShortMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2ShortMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2ShortMap.Entry belowSamplesLesser() { return new AbstractChar2ShortMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2ShortMap.Entry belowSamplesGreater() { return new AbstractChar2ShortMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2ShortMap.Entry aboveSamplesLesser() { return new AbstractChar2ShortMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2ShortMap.Entry aboveSamplesGreater() { return new AbstractChar2ShortMap.BasicEntry(keys[8], values[8]); }
	}
}
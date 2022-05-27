package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2BooleanMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2BooleanSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleChar2BooleanMapTestGenerator<E extends Char2BooleanMap>
{
	BiFunction<char[], boolean[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	boolean[] values = new boolean[]{true, false, true, false, true, false, true, false, true};
	
	public SimpleChar2BooleanMapTestGenerator(BiFunction<char[], boolean[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2BooleanMap.Entry> getSamples() {
		return new ObjectSamples<Char2BooleanMap.Entry>(
			new AbstractChar2BooleanMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2BooleanMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2BooleanMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2BooleanMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2BooleanMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2BooleanMap.Entry... elements) {
		char[] keys = new char[elements.length];
		boolean[] values = new boolean[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getBooleanValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Boolean>> order(List<Map.Entry<Character, Boolean>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2BooleanMap.Entry> order(ObjectList<Char2BooleanMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2BooleanMapTestGenerator<Char2BooleanMap> implements TestChar2BooleanMapGenerator
	{
		public Maps(BiFunction<char[], boolean[], Char2BooleanMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2BooleanMapTestGenerator<Char2BooleanSortedMap> implements TestChar2BooleanSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], boolean[], Char2BooleanSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Boolean>> order(List<Map.Entry<Character, Boolean>> insertionOrder) {
			insertionOrder.sort(DerivedChar2BooleanMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2BooleanMap.Entry> order(ObjectList<Char2BooleanMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2BooleanMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2BooleanMap.Entry belowSamplesLesser() { return new AbstractChar2BooleanMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2BooleanMap.Entry belowSamplesGreater() { return new AbstractChar2BooleanMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2BooleanMap.Entry aboveSamplesLesser() { return new AbstractChar2BooleanMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2BooleanMap.Entry aboveSamplesGreater() { return new AbstractChar2BooleanMap.BasicEntry(keys[8], values[8]); }
	}
}
package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2CharMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleChar2CharMapTestGenerator<E extends Char2CharMap>
{
	BiFunction<char[], char[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleChar2CharMapTestGenerator(BiFunction<char[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2CharMap.Entry> getSamples() {
		return new ObjectSamples<Char2CharMap.Entry>(
			new AbstractChar2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2CharMap.Entry... elements) {
		char[] keys = new char[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Character>> order(List<Map.Entry<Character, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2CharMap.Entry> order(ObjectList<Char2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2CharMapTestGenerator<Char2CharMap> implements TestChar2CharMapGenerator
	{
		public Maps(BiFunction<char[], char[], Char2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2CharMapTestGenerator<Char2CharSortedMap> implements TestChar2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], char[], Char2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Character>> order(List<Map.Entry<Character, Character>> insertionOrder) {
			insertionOrder.sort(DerivedChar2CharMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2CharMap.Entry> order(ObjectList<Char2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2CharMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2CharMap.Entry belowSamplesLesser() { return new AbstractChar2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2CharMap.Entry belowSamplesGreater() { return new AbstractChar2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2CharMap.Entry aboveSamplesLesser() { return new AbstractChar2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2CharMap.Entry aboveSamplesGreater() { return new AbstractChar2CharMap.BasicEntry(keys[8], values[8]); }
	}
}
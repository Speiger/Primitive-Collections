package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2FloatMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2FloatSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleChar2FloatMapTestGenerator<E extends Char2FloatMap>
{
	BiFunction<char[], float[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	float[] values = new float[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleChar2FloatMapTestGenerator(BiFunction<char[], float[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2FloatMap.Entry> getSamples() {
		return new ObjectSamples<Char2FloatMap.Entry>(
			new AbstractChar2FloatMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2FloatMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2FloatMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2FloatMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2FloatMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2FloatMap.Entry... elements) {
		char[] keys = new char[elements.length];
		float[] values = new float[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getFloatValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Float>> order(List<Map.Entry<Character, Float>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2FloatMap.Entry> order(ObjectList<Char2FloatMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2FloatMapTestGenerator<Char2FloatMap> implements TestChar2FloatMapGenerator
	{
		public Maps(BiFunction<char[], float[], Char2FloatMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2FloatMapTestGenerator<Char2FloatSortedMap> implements TestChar2FloatSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], float[], Char2FloatSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Float>> order(List<Map.Entry<Character, Float>> insertionOrder) {
			insertionOrder.sort(DerivedChar2FloatMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2FloatMap.Entry> order(ObjectList<Char2FloatMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2FloatMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2FloatMap.Entry belowSamplesLesser() { return new AbstractChar2FloatMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2FloatMap.Entry belowSamplesGreater() { return new AbstractChar2FloatMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2FloatMap.Entry aboveSamplesLesser() { return new AbstractChar2FloatMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2FloatMap.Entry aboveSamplesGreater() { return new AbstractChar2FloatMap.BasicEntry(keys[8], values[8]); }
	}
}
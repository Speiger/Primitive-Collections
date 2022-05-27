package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2DoubleMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2DoubleSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class SimpleChar2DoubleMapTestGenerator<E extends Char2DoubleMap>
{
	BiFunction<char[], double[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	double[] values = new double[]{0, 1, 2, 3, 4, -2, -1, 5, 6};
	
	public SimpleChar2DoubleMapTestGenerator(BiFunction<char[], double[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2DoubleMap.Entry> getSamples() {
		return new ObjectSamples<Char2DoubleMap.Entry>(
			new AbstractChar2DoubleMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2DoubleMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2DoubleMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2DoubleMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2DoubleMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2DoubleMap.Entry... elements) {
		char[] keys = new char[elements.length];
		double[] values = new double[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getDoubleValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Double>> order(List<Map.Entry<Character, Double>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2DoubleMap.Entry> order(ObjectList<Char2DoubleMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2DoubleMapTestGenerator<Char2DoubleMap> implements TestChar2DoubleMapGenerator
	{
		public Maps(BiFunction<char[], double[], Char2DoubleMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2DoubleMapTestGenerator<Char2DoubleSortedMap> implements TestChar2DoubleSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], double[], Char2DoubleSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Double>> order(List<Map.Entry<Character, Double>> insertionOrder) {
			insertionOrder.sort(DerivedChar2DoubleMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2DoubleMap.Entry> order(ObjectList<Char2DoubleMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2DoubleMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2DoubleMap.Entry belowSamplesLesser() { return new AbstractChar2DoubleMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2DoubleMap.Entry belowSamplesGreater() { return new AbstractChar2DoubleMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2DoubleMap.Entry aboveSamplesLesser() { return new AbstractChar2DoubleMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2DoubleMap.Entry aboveSamplesGreater() { return new AbstractChar2DoubleMap.BasicEntry(keys[8], values[8]); }
	}
}
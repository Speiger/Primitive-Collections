package speiger.src.testers.chars.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.generators.maps.TestChar2ByteMapGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ByteSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleChar2ByteMapTestGenerator<E extends Char2ByteMap>
{
	BiFunction<char[], byte[], E> mapper;
	char[] keys = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	byte[] values = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	
	public SimpleChar2ByteMapTestGenerator(BiFunction<char[], byte[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Char2ByteMap.Entry> getSamples() {
		return new ObjectSamples<Char2ByteMap.Entry>(
			new AbstractChar2ByteMap.BasicEntry(keys[0], values[0]),
			new AbstractChar2ByteMap.BasicEntry(keys[1], values[1]),
			new AbstractChar2ByteMap.BasicEntry(keys[2], values[2]),
			new AbstractChar2ByteMap.BasicEntry(keys[3], values[3]),
			new AbstractChar2ByteMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Char2ByteMap.Entry... elements) {
		char[] keys = new char[elements.length];
		byte[] values = new byte[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getCharKey();
			values[i] = elements[i].getByteValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Character, Byte>> order(List<Map.Entry<Character, Byte>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Char2ByteMap.Entry> order(ObjectList<Char2ByteMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleChar2ByteMapTestGenerator<Char2ByteMap> implements TestChar2ByteMapGenerator
	{
		public Maps(BiFunction<char[], byte[], Char2ByteMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleChar2ByteMapTestGenerator<Char2ByteSortedMap> implements TestChar2ByteSortedMapGenerator
	{
		public SortedMaps(BiFunction<char[], byte[], Char2ByteSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Character, Byte>> order(List<Map.Entry<Character, Byte>> insertionOrder) {
			insertionOrder.sort(DerivedChar2ByteMapGenerators.entryObjectComparator(Character::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Char2ByteMap.Entry> order(ObjectList<Char2ByteMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedChar2ByteMapGenerators.entryComparator(Character::compare));
			return insertionOrder;
		}
		
		@Override
		public Char2ByteMap.Entry belowSamplesLesser() { return new AbstractChar2ByteMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Char2ByteMap.Entry belowSamplesGreater() { return new AbstractChar2ByteMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Char2ByteMap.Entry aboveSamplesLesser() { return new AbstractChar2ByteMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Char2ByteMap.Entry aboveSamplesGreater() { return new AbstractChar2ByteMap.BasicEntry(keys[8], values[8]); }
	}
}
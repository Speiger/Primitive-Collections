package speiger.src.testers.bytes.impl.maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharSortedMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.generators.maps.TestByte2CharMapGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2CharSortedMapGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleByte2CharMapTestGenerator<E extends Byte2CharMap>
{
	BiFunction<byte[], char[], E> mapper;
	byte[] keys = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)-2, (byte)-1, (byte)5, (byte)6};
	char[] values = new char[]{'a', 'b', 'c', 'd', 'e', '_', '`', 'f', 'g'};
	
	public SimpleByte2CharMapTestGenerator(BiFunction<byte[], char[], E> mapper) {
		this.mapper = mapper;
	}
	
	public ObjectSamples<Byte2CharMap.Entry> getSamples() {
		return new ObjectSamples<Byte2CharMap.Entry>(
			new AbstractByte2CharMap.BasicEntry(keys[0], values[0]),
			new AbstractByte2CharMap.BasicEntry(keys[1], values[1]),
			new AbstractByte2CharMap.BasicEntry(keys[2], values[2]),
			new AbstractByte2CharMap.BasicEntry(keys[3], values[3]),
			new AbstractByte2CharMap.BasicEntry(keys[4], values[4])
		);
	}
	
	public E create(Byte2CharMap.Entry... elements) {
		byte[] keys = new byte[elements.length];
		char[] values = new char[elements.length];
		for(int i = 0;i<elements.length;i++) {
			keys[i] = elements[i].getByteKey();
			values[i] = elements[i].getCharValue();
		}
		return mapper.apply(keys, values);
	}
	
	public Iterable<Map.Entry<Byte, Character>> order(List<Map.Entry<Byte, Character>> insertionOrder) {
		return insertionOrder;
	}
	
	public ObjectIterable<Byte2CharMap.Entry> order(ObjectList<Byte2CharMap.Entry> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Maps extends SimpleByte2CharMapTestGenerator<Byte2CharMap> implements TestByte2CharMapGenerator
	{
		public Maps(BiFunction<byte[], char[], Byte2CharMap> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedMaps extends SimpleByte2CharMapTestGenerator<Byte2CharSortedMap> implements TestByte2CharSortedMapGenerator
	{
		public SortedMaps(BiFunction<byte[], char[], Byte2CharSortedMap> mapper) {
			super(mapper);
		}
		
		public Iterable<Map.Entry<Byte, Character>> order(List<Map.Entry<Byte, Character>> insertionOrder) {
			insertionOrder.sort(DerivedByte2CharMapGenerators.entryObjectComparator(Byte::compare));
			return insertionOrder;
		}
		
		public ObjectIterable<Byte2CharMap.Entry> order(ObjectList<Byte2CharMap.Entry> insertionOrder) {
			insertionOrder.sort(DerivedByte2CharMapGenerators.entryComparator(Byte::compare));
			return insertionOrder;
		}
		
		@Override
		public Byte2CharMap.Entry belowSamplesLesser() { return new AbstractByte2CharMap.BasicEntry(keys[5], values[5]); }
		@Override
		public Byte2CharMap.Entry belowSamplesGreater() { return new AbstractByte2CharMap.BasicEntry(keys[6], values[6]); }
		@Override
		public Byte2CharMap.Entry aboveSamplesLesser() { return new AbstractByte2CharMap.BasicEntry(keys[7], values[7]); }
		@Override
		public Byte2CharMap.Entry aboveSamplesGreater() { return new AbstractByte2CharMap.BasicEntry(keys[8], values[8]); }
	}
}
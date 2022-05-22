package speiger.src.testers.bytes.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.generators.TestByteListGenerator;
import speiger.src.testers.bytes.generators.TestByteNavigableSetGenerator;
import speiger.src.testers.bytes.generators.TestByteOrderedSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;

public class SimpleByteTestGenerator<T extends ByteCollection> {
	Function<byte[], T> mapper;
	
	public SimpleByteTestGenerator(Function<byte[], T> mapper) {
		this.mapper = mapper;
	}
	
	public ByteSamples getSamples() {
		return new ByteSamples((byte)1, (byte)0, (byte)2, (byte)3, (byte)4);
	}
	
	public T create(byte... elements) {
		return mapper.apply(elements);
	}
	
	public T create(Object... elements) {
		byte[] array = new byte[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Byte)e).byteValue();
		}
		return mapper.apply(array);
	}
	
	public ByteIterable order(ByteList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Byte> order(List<Byte> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleByteTestGenerator<ByteCollection> implements TestByteCollectionGenerator
	{
		public Collections(Function<byte[], ByteCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleByteTestGenerator<ByteList> implements TestByteListGenerator
	{
		public Lists(Function<byte[], ByteList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleByteTestGenerator<ByteSet> implements TestByteSetGenerator
	{
		public Sets(Function<byte[], ByteSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleByteTestGenerator<ByteOrderedSet> implements TestByteOrderedSetGenerator
	{
		public OrderedSets(Function<byte[], ByteOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleByteTestGenerator<ByteSortedSet> implements TestByteSortedSetGenerator
	{
		public SortedSets(Function<byte[], ByteSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public ByteIterable order(ByteList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public byte belowSamplesLesser() { return -2; }
		@Override
		public byte belowSamplesGreater() { return -1; }
		@Override
		public byte aboveSamplesLesser() { return 5; }
		@Override
		public byte aboveSamplesGreater() { return 6; }
	}
	
	public static class NavigableSets extends SimpleByteTestGenerator<ByteNavigableSet> implements TestByteNavigableSetGenerator
	{
		public NavigableSets(Function<byte[], ByteNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public ByteIterable order(ByteList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public byte belowSamplesLesser() { return -2; }
		@Override
		public byte belowSamplesGreater() { return -1; }
		@Override
		public byte aboveSamplesLesser() { return 5; }
		@Override
		public byte aboveSamplesGreater() { return 6; }
	}
}
package speiger.src.testers.chars.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.chars.generators.TestCharListGenerator;
import speiger.src.testers.chars.generators.TestCharNavigableSetGenerator;
import speiger.src.testers.chars.generators.TestCharOrderedSetGenerator;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.utils.CharSamples;

public class SimpleCharTestGenerator<T extends CharCollection> {
	Function<char[], T> mapper;
	
	public SimpleCharTestGenerator(Function<char[], T> mapper) {
		this.mapper = mapper;
	}
	
	public CharSamples getSamples() {
		return new CharSamples('b', 'a', 'c', 'd', 'e');
	}
	
	public T create(char... elements) {
		return mapper.apply(elements);
	}
	
	public T create(Object... elements) {
		char[] array = new char[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Character)e).charValue();
		}
		return mapper.apply(array);
	}
	
	public CharIterable order(CharList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Character> order(List<Character> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleCharTestGenerator<CharCollection> implements TestCharCollectionGenerator
	{
		public Collections(Function<char[], CharCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleCharTestGenerator<CharList> implements TestCharListGenerator
	{
		public Lists(Function<char[], CharList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleCharTestGenerator<CharSet> implements TestCharSetGenerator
	{
		public Sets(Function<char[], CharSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleCharTestGenerator<CharOrderedSet> implements TestCharOrderedSetGenerator
	{
		public OrderedSets(Function<char[], CharOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleCharTestGenerator<CharSortedSet> implements TestCharSortedSetGenerator
	{
		public SortedSets(Function<char[], CharSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public CharIterable order(CharList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public char belowSamplesLesser() { return '_'; }
		@Override
		public char belowSamplesGreater() { return '`'; }
		@Override
		public char aboveSamplesLesser() { return 'f'; }
		@Override
		public char aboveSamplesGreater() { return 'g'; }
	}
	
	public static class NavigableSets extends SimpleCharTestGenerator<CharNavigableSet> implements TestCharNavigableSetGenerator
	{
		public NavigableSets(Function<char[], CharNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public CharIterable order(CharList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public char belowSamplesLesser() { return '_'; }
		@Override
		public char belowSamplesGreater() { return '`'; }
		@Override
		public char aboveSamplesLesser() { return 'f'; }
		@Override
		public char aboveSamplesGreater() { return 'g'; }
	}
}
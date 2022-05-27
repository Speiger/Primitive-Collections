package speiger.src.testers.chars.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharSortedSet;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.chars.utils.CharSamples;

public class CharSortedSetSubsetTestSetGenerator implements TestCharSortedSetGenerator
{
	final Bound to;
	final Bound from;
	final char firstInclusive;
	final char lastInclusive;
	private final CharComparator comparator;
	private final TestCharSortedSetGenerator delegate;

	public CharSortedSetSubsetTestSetGenerator(TestCharSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		CharSortedSet emptySet = delegate.create(new char[0]);
		comparator = emptySet.comparator();

		CharSamples samples = delegate.getSamples();
		CharList samplesList = new CharArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getChar(0);
		lastInclusive = samplesList.getChar(samplesList.size() - 1);
	}

	public final TestCharSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	CharSortedSet createSubSet(CharSortedSet set, char firstExclusive, char lastExclusive) {
		if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
			return set.headSet(lastExclusive);
		} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
			return set.tailSet(firstInclusive);
		} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
			return set.subSet(firstInclusive, lastExclusive);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public char belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public char belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public char aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public char aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CharSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public CharIterable order(CharList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Character> order(List<Character> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public CharSortedSet create(char... elements) {
		CharList normalValues = CharArrayList.wrap(elements);
		CharList extremeValues = new CharArrayList();
		char firstExclusive = delegate.belowSamplesGreater();
		char lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		CharList allEntries = new CharArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		CharSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}
	
	@Override
	public CharSortedSet create(Object... elements) {
		char[] array = new char[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Character)e).charValue();
		}
		return create(array);
	}

	public static final class CharNavigableSetSubsetTestSetGenerator extends CharSortedSetSubsetTestSetGenerator {
		public CharNavigableSetSubsetTestSetGenerator(TestCharSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		CharNavigableSet createSubSet(CharSortedSet sortedSet, char firstExclusive, char lastExclusive) {
			CharNavigableSet set = (CharNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (CharNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
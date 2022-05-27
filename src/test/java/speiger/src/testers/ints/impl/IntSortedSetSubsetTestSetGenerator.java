package speiger.src.testers.ints.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.utils.IntSamples;

public class IntSortedSetSubsetTestSetGenerator implements TestIntSortedSetGenerator
{
	final Bound to;
	final Bound from;
	final int firstInclusive;
	final int lastInclusive;
	private final IntComparator comparator;
	private final TestIntSortedSetGenerator delegate;

	public IntSortedSetSubsetTestSetGenerator(TestIntSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		IntSortedSet emptySet = delegate.create(new int[0]);
		comparator = emptySet.comparator();

		IntSamples samples = delegate.getSamples();
		IntList samplesList = new IntArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getInt(0);
		lastInclusive = samplesList.getInt(samplesList.size() - 1);
	}

	public final TestIntSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	IntSortedSet createSubSet(IntSortedSet set, int firstExclusive, int lastExclusive) {
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
	public int belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public IntIterable order(IntList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Integer> order(List<Integer> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public IntSortedSet create(int... elements) {
		IntList normalValues = IntArrayList.wrap(elements);
		IntList extremeValues = new IntArrayList();
		int firstExclusive = delegate.belowSamplesGreater();
		int lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		IntList allEntries = new IntArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		IntSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}
	
	@Override
	public IntSortedSet create(Object... elements) {
		int[] array = new int[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Integer)e).intValue();
		}
		return create(array);
	}

	public static final class IntNavigableSetSubsetTestSetGenerator extends IntSortedSetSubsetTestSetGenerator {
		public IntNavigableSetSubsetTestSetGenerator(TestIntSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		IntNavigableSet createSubSet(IntSortedSet sortedSet, int firstExclusive, int lastExclusive) {
			IntNavigableSet set = (IntNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (IntNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
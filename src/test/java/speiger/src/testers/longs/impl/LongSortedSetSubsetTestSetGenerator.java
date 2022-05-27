package speiger.src.testers.longs.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.longs.utils.LongSamples;

public class LongSortedSetSubsetTestSetGenerator implements TestLongSortedSetGenerator
{
	final Bound to;
	final Bound from;
	final long firstInclusive;
	final long lastInclusive;
	private final LongComparator comparator;
	private final TestLongSortedSetGenerator delegate;

	public LongSortedSetSubsetTestSetGenerator(TestLongSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		LongSortedSet emptySet = delegate.create(new long[0]);
		comparator = emptySet.comparator();

		LongSamples samples = delegate.getSamples();
		LongList samplesList = new LongArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getLong(0);
		lastInclusive = samplesList.getLong(samplesList.size() - 1);
	}

	public final TestLongSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	LongSortedSet createSubSet(LongSortedSet set, long firstExclusive, long lastExclusive) {
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
	public long belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public LongSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public LongIterable order(LongList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Long> order(List<Long> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public LongSortedSet create(long... elements) {
		LongList normalValues = LongArrayList.wrap(elements);
		LongList extremeValues = new LongArrayList();
		long firstExclusive = delegate.belowSamplesGreater();
		long lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		LongList allEntries = new LongArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		LongSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}
	
	@Override
	public LongSortedSet create(Object... elements) {
		long[] array = new long[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Long)e).longValue();
		}
		return create(array);
	}

	public static final class LongNavigableSetSubsetTestSetGenerator extends LongSortedSetSubsetTestSetGenerator {
		public LongNavigableSetSubsetTestSetGenerator(TestLongSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		LongNavigableSet createSubSet(LongSortedSet sortedSet, long firstExclusive, long lastExclusive) {
			LongNavigableSet set = (LongNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (LongNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
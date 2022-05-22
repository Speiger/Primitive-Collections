package speiger.src.testers.shorts.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.testers.shorts.generators.TestShortSortedSetGenerator;
import speiger.src.testers.shorts.utils.ShortSamples;

public class ShortSortedSetSubsetTestSetGenerator implements TestShortSortedSetGenerator {

	final Bound to;
	final Bound from;
	final short firstInclusive;
	final short lastInclusive;
	private final ShortComparator comparator;
	private final TestShortSortedSetGenerator delegate;

	public ShortSortedSetSubsetTestSetGenerator(TestShortSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		ShortSortedSet emptySet = delegate.create(new short[0]);
		comparator = emptySet.comparator();

		ShortSamples samples = delegate.getSamples();
		ShortList samplesList = new ShortArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getShort(0);
		lastInclusive = samplesList.getShort(samplesList.size() - 1);
	}

	public final TestShortSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	ShortSortedSet createSubSet(ShortSortedSet set, short firstExclusive, short lastExclusive) {
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
	public short belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public short aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ShortSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public ShortIterable order(ShortList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Short> order(List<Short> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public ShortSortedSet create(short... elements) {
		ShortList normalValues = ShortArrayList.wrap(elements);
		ShortList extremeValues = new ShortArrayList();
		short firstExclusive = delegate.belowSamplesGreater();
		short lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		ShortList allEntries = new ShortArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		ShortSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}

	@Override
	public ShortSortedSet create(Object... elements) {
		short[] array = new short[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Short)e).shortValue();
		}
		return create(array);
	}

	public static final class ShortNavigableSetSubsetTestSetGenerator extends ShortSortedSetSubsetTestSetGenerator {
		public ShortNavigableSetSubsetTestSetGenerator(TestShortSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		ShortNavigableSet createSubSet(ShortSortedSet sortedSet, short firstExclusive, short lastExclusive) {
			ShortNavigableSet set = (ShortNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (ShortNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
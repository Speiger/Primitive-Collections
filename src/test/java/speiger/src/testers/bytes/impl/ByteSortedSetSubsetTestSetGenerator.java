package speiger.src.testers.bytes.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.testers.bytes.generators.TestByteSortedSetGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;

public class ByteSortedSetSubsetTestSetGenerator implements TestByteSortedSetGenerator {

	final Bound to;
	final Bound from;
	final byte firstInclusive;
	final byte lastInclusive;
	private final ByteComparator comparator;
	private final TestByteSortedSetGenerator delegate;

	public ByteSortedSetSubsetTestSetGenerator(TestByteSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		ByteSortedSet emptySet = delegate.create(new byte[0]);
		comparator = emptySet.comparator();

		ByteSamples samples = delegate.getSamples();
		ByteList samplesList = new ByteArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getByte(0);
		lastInclusive = samplesList.getByte(samplesList.size() - 1);
	}

	public final TestByteSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	ByteSortedSet createSubSet(ByteSortedSet set, byte firstExclusive, byte lastExclusive) {
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
	public byte belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ByteSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public ByteIterable order(ByteList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Byte> order(List<Byte> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public ByteSortedSet create(byte... elements) {
		ByteList normalValues = ByteArrayList.wrap(elements);
		ByteList extremeValues = new ByteArrayList();
		byte firstExclusive = delegate.belowSamplesGreater();
		byte lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		ByteList allEntries = new ByteArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		ByteSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}

	@Override
	public ByteSortedSet create(Object... elements) {
		byte[] array = new byte[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Byte)e).byteValue();
		}
		return create(array);
	}

	public static final class ByteNavigableSetSubsetTestSetGenerator extends ByteSortedSetSubsetTestSetGenerator {
		public ByteNavigableSetSubsetTestSetGenerator(TestByteSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		ByteNavigableSet createSubSet(ByteSortedSet sortedSet, byte firstExclusive, byte lastExclusive) {
			ByteNavigableSet set = (ByteNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (ByteNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
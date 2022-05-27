package speiger.src.testers.doubles.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.testers.doubles.generators.TestDoubleSortedSetGenerator;
import speiger.src.testers.doubles.utils.DoubleSamples;

@SuppressWarnings("javadoc")
public class DoubleSortedSetSubsetTestSetGenerator implements TestDoubleSortedSetGenerator
{
	final Bound to;
	final Bound from;
	final double firstInclusive;
	final double lastInclusive;
	private final DoubleComparator comparator;
	private final TestDoubleSortedSetGenerator delegate;

	public DoubleSortedSetSubsetTestSetGenerator(TestDoubleSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		DoubleSortedSet emptySet = delegate.create(new double[0]);
		comparator = emptySet.comparator();

		DoubleSamples samples = delegate.getSamples();
		DoubleList samplesList = new DoubleArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getDouble(0);
		lastInclusive = samplesList.getDouble(samplesList.size() - 1);
	}

	public final TestDoubleSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	DoubleSortedSet createSubSet(DoubleSortedSet set, double firstExclusive, double lastExclusive) {
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
	public double belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public DoubleSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public DoubleIterable order(DoubleList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Double> order(List<Double> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public DoubleSortedSet create(double... elements) {
		DoubleList normalValues = DoubleArrayList.wrap(elements);
		DoubleList extremeValues = new DoubleArrayList();
		double firstExclusive = delegate.belowSamplesGreater();
		double lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		DoubleList allEntries = new DoubleArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		DoubleSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}
	
	@Override
	public DoubleSortedSet create(Object... elements) {
		double[] array = new double[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Double)e).doubleValue();
		}
		return create(array);
	}

	public static final class DoubleNavigableSetSubsetTestSetGenerator extends DoubleSortedSetSubsetTestSetGenerator {
		public DoubleNavigableSetSubsetTestSetGenerator(TestDoubleSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		DoubleNavigableSet createSubSet(DoubleSortedSet sortedSet, double firstExclusive, double lastExclusive) {
			DoubleNavigableSet set = (DoubleNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (DoubleNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
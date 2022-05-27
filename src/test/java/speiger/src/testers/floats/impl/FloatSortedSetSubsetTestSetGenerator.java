package speiger.src.testers.floats.impl;

import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatSortedSet;
import speiger.src.testers.floats.generators.TestFloatSortedSetGenerator;
import speiger.src.testers.floats.utils.FloatSamples;

@SuppressWarnings("javadoc")
public class FloatSortedSetSubsetTestSetGenerator implements TestFloatSortedSetGenerator
{
	final Bound to;
	final Bound from;
	final float firstInclusive;
	final float lastInclusive;
	private final FloatComparator comparator;
	private final TestFloatSortedSetGenerator delegate;

	public FloatSortedSetSubsetTestSetGenerator(TestFloatSortedSetGenerator delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		FloatSortedSet emptySet = delegate.create(new float[0]);
		comparator = emptySet.comparator();

		FloatSamples samples = delegate.getSamples();
		FloatList samplesList = new FloatArrayList(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.getFloat(0);
		lastInclusive = samplesList.getFloat(samplesList.size() - 1);
	}

	public final TestFloatSortedSetGenerator getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	FloatSortedSet createSubSet(FloatSortedSet set, float firstExclusive, float lastExclusive) {
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
	public float belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FloatSamples getSamples() {
		return delegate.getSamples();
	}

	@Override
	public FloatIterable order(FloatList insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<Float> order(List<Float> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public FloatSortedSet create(float... elements) {
		FloatList normalValues = FloatArrayList.wrap(elements);
		FloatList extremeValues = new FloatArrayList();
		float firstExclusive = delegate.belowSamplesGreater();
		float lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		FloatList allEntries = new FloatArrayList();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		FloatSortedSet set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}
	
	@Override
	public FloatSortedSet create(Object... elements) {
		float[] array = new float[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Float)e).floatValue();
		}
		return create(array);
	}

	public static final class FloatNavigableSetSubsetTestSetGenerator extends FloatSortedSetSubsetTestSetGenerator {
		public FloatNavigableSetSubsetTestSetGenerator(TestFloatSortedSetGenerator delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		FloatNavigableSet createSubSet(FloatSortedSet sortedSet, float firstExclusive, float lastExclusive) {
			FloatNavigableSet set = (FloatNavigableSet) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (FloatNavigableSet)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
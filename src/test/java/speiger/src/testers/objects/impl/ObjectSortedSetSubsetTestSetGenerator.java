package speiger.src.testers.objects.impl;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class ObjectSortedSetSubsetTestSetGenerator<T> implements TestObjectSortedSetGenerator<T>
{
	final Bound to;
	final Bound from;
	final T firstInclusive;
	final T lastInclusive;
	private final Comparator<T> comparator;
	private final TestObjectSortedSetGenerator<T> delegate;

	public ObjectSortedSetSubsetTestSetGenerator(TestObjectSortedSetGenerator<T> delegate, Bound to, Bound from) {
		this.to = to;
		this.from = from;
		this.delegate = delegate;

		ObjectSortedSet<T> emptySet = delegate.create((T[])new Object[0]);
		comparator = emptySet.comparator();

		ObjectSamples<T> samples = delegate.getSamples();
		ObjectList<T> samplesList = new ObjectArrayList<>(samples.asList());
		samplesList.sort(comparator);
		firstInclusive = samplesList.get(0);
		lastInclusive = samplesList.get(samplesList.size() - 1);
	}

	public final TestObjectSortedSetGenerator<T> getInnerGenerator() {
		return delegate;
	}

	public final Bound getTo() {
		return to;
	}

	public final Bound getFrom() {
		return from;
	}

	ObjectSortedSet<T> createSubSet(ObjectSortedSet<T> set, T firstExclusive, T lastExclusive) {
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
	public T belowSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T belowSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T aboveSamplesLesser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T aboveSamplesGreater() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ObjectSamples<T> getSamples() {
		return delegate.getSamples();
	}

	@Override
	public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public Iterable<T> order(List<T> insertionOrder) {
		return delegate.order(insertionOrder);
	}

	@Override
	public ObjectSortedSet<T> create(Object... elements) {
		ObjectList<T> normalValues = (ObjectList<T>)ObjectArrayList.wrap(elements);
		ObjectList<T> extremeValues = new ObjectArrayList<>();
		T firstExclusive = delegate.belowSamplesGreater();
		T lastExclusive = delegate.aboveSamplesLesser();
		if (from != Bound.NO_BOUND) {
			extremeValues.add(delegate.belowSamplesLesser());
			extremeValues.add(delegate.belowSamplesGreater());
		}
		if (to != Bound.NO_BOUND) {
			extremeValues.add(delegate.aboveSamplesLesser());
			extremeValues.add(delegate.aboveSamplesGreater());
		}
		ObjectList<T> allEntries = new ObjectArrayList<>();
		allEntries.addAll(extremeValues);
		allEntries.addAll(normalValues);
		ObjectSortedSet<T> set = delegate.create(allEntries.toArray());

		return createSubSet(set, firstExclusive, lastExclusive);
	}
	
	public static final class ObjectNavigableSetSubsetTestSetGenerator<T> extends ObjectSortedSetSubsetTestSetGenerator<T> {
		public ObjectNavigableSetSubsetTestSetGenerator(TestObjectSortedSetGenerator<T> delegate, Bound to, Bound from) {
			super(delegate, to, from);
		}
		
		@Override
		ObjectNavigableSet<T> createSubSet(ObjectSortedSet<T> sortedSet, T firstExclusive, T lastExclusive) {
			ObjectNavigableSet<T> set = (ObjectNavigableSet<T>) sortedSet;
			if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) return set.headSet(lastInclusive, true);	
			else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) return set.tailSet(firstExclusive, false);	
			else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) return set.subSet(firstExclusive, false, lastExclusive, false);
			else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstExclusive, false, lastInclusive, true);
			else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) return set.subSet(firstInclusive, true, lastInclusive, true);
			return (ObjectNavigableSet<T>)super.createSubSet(set, firstExclusive, lastExclusive);

		}
	}
}
package speiger.src.testers.ints.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.utils.IntLists;

public abstract class IntIteratorTester extends AbstractIntIteratorTester
{

	public IntIteratorTester(int steps, Iterable<? extends IteratorFeature> features, IntIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, IntLists.singleton(-1), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<IntIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
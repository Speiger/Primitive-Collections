package speiger.src.testers.longs.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.utils.LongLists;

public abstract class LongIteratorTester extends AbstractLongIteratorTester {

	public LongIteratorTester(int steps, Iterable<? extends IteratorFeature> features, LongIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, LongLists.singleton(-1L), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<LongIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
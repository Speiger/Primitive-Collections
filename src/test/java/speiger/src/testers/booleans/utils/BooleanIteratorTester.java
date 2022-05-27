package speiger.src.testers.booleans.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.utils.BooleanLists;

@SuppressWarnings("javadoc")
public abstract class BooleanIteratorTester extends AbstractBooleanIteratorTester
{
	public BooleanIteratorTester(int steps, Iterable<? extends IteratorFeature> features, BooleanIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, BooleanLists.singleton(false), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<BooleanIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
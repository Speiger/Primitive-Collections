package speiger.src.testers.floats.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.utils.FloatLists;

@SuppressWarnings("javadoc")
public abstract class FloatIteratorTester extends AbstractFloatIteratorTester
{
	public FloatIteratorTester(int steps, Iterable<? extends IteratorFeature> features, FloatIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, FloatLists.singleton(-1F), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<FloatIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
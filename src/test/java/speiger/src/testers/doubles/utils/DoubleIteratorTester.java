package speiger.src.testers.doubles.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.utils.DoubleLists;

@SuppressWarnings("javadoc")
public abstract class DoubleIteratorTester extends AbstractDoubleIteratorTester
{
	public DoubleIteratorTester(int steps, Iterable<? extends IteratorFeature> features, DoubleIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, DoubleLists.singleton(-1D), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<DoubleIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
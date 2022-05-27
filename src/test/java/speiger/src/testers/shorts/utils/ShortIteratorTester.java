package speiger.src.testers.shorts.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.utils.ShortLists;

@SuppressWarnings("javadoc")
public abstract class ShortIteratorTester extends AbstractShortIteratorTester
{
	public ShortIteratorTester(int steps, Iterable<? extends IteratorFeature> features, ShortIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, ShortLists.singleton((short)-1), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<ShortIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
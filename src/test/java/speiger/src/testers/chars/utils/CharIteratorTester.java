package speiger.src.testers.chars.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.utils.CharLists;

public abstract class CharIteratorTester extends AbstractCharIteratorTester
{

	public CharIteratorTester(int steps, Iterable<? extends IteratorFeature> features, CharIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, CharLists.singleton((char)-1), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<CharIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
package speiger.src.testers.bytes.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.utils.ByteLists;

public abstract class ByteIteratorTester extends AbstractByteIteratorTester {

	public ByteIteratorTester(int steps, Iterable<? extends IteratorFeature> features, ByteIterable expectedElements,
			KnownOrder knownOrder) {
		super(steps, ByteLists.singleton((byte)-1), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<ByteIterator>> getStimulusValues() {
		return iteratorStimuli();
	}
}
package speiger.src.testers.objects.utils;

import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectLists;

@SuppressWarnings("javadoc")
public abstract class ObjectIteratorTester<T> extends AbstractObjectIteratorTester<T>
{
	public ObjectIteratorTester(int steps, Iterable<? extends IteratorFeature> features, ObjectIterable<T> expectedElements,
			KnownOrder knownOrder) {
		super(steps, ObjectLists.singleton(null), features, expectedElements, knownOrder, 0);
	}

	@Override
	protected Iterable<? extends Stimulus<ObjectIterator<?>>> getStimulusValues() {
		return iteratorStimuli();
	}
}
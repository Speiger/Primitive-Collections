package speiger.src.testers.objects.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;

@SuppressWarnings("javadoc")
public abstract class ObjectListIteratorTester<T> extends AbstractObjectIteratorTester<T> {

	public ObjectListIteratorTester(int steps, ObjectIterable<T> elementsToInsertIterable, Iterable<? extends IteratorFeature> features, ObjectIterable<T> expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<ObjectIterator<?>>> getStimulusValues() {
	    List<Stimulus<ObjectIterator<?>>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<ObjectIterator<?>>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<ObjectIterator<?>>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract ObjectListIterator<T> newTargetIterator();
}
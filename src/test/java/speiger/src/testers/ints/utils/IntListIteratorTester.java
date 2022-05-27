package speiger.src.testers.ints.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntListIterator;

@SuppressWarnings("javadoc")
public abstract class IntListIteratorTester extends AbstractIntIteratorTester {

	public IntListIteratorTester(int steps, IntIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, IntIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<IntIterator>> getStimulusValues() {
	    List<Stimulus<IntIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<IntIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<IntIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract IntListIterator newTargetIterator();
}
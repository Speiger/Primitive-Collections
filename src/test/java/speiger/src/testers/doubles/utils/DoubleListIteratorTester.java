package speiger.src.testers.doubles.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.lists.DoubleListIterator;

@SuppressWarnings("javadoc")
public abstract class DoubleListIteratorTester extends AbstractDoubleIteratorTester {

	public DoubleListIteratorTester(int steps, DoubleIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, DoubleIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<DoubleIterator>> getStimulusValues() {
	    List<Stimulus<DoubleIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<DoubleIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<DoubleIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract DoubleListIterator newTargetIterator();
}
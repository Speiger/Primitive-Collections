package speiger.src.testers.floats.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.lists.FloatListIterator;

public abstract class FloatListIteratorTester extends AbstractFloatIteratorTester {

	public FloatListIteratorTester(int steps, FloatIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, FloatIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<FloatIterator>> getStimulusValues() {
	    List<Stimulus<FloatIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<FloatIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<FloatIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract FloatListIterator newTargetIterator();
}
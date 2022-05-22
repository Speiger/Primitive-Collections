package speiger.src.testers.booleans.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanListIterator;

public abstract class BooleanListIteratorTester extends AbstractBooleanIteratorTester {

	public BooleanListIteratorTester(int steps, BooleanIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, BooleanIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<BooleanIterator>> getStimulusValues() {
	    List<Stimulus<BooleanIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<BooleanIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<BooleanIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract BooleanListIterator newTargetIterator();
}
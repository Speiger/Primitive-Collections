package speiger.src.testers.shorts.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.lists.ShortListIterator;

public abstract class ShortListIteratorTester extends AbstractShortIteratorTester {

	public ShortListIteratorTester(int steps, ShortIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, ShortIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<ShortIterator>> getStimulusValues() {
	    List<Stimulus<ShortIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<ShortIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<ShortIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract ShortListIterator newTargetIterator();
}
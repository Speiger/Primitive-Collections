package speiger.src.testers.longs.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongListIterator;

@SuppressWarnings("javadoc")
public abstract class LongListIteratorTester extends AbstractLongIteratorTester {

	public LongListIteratorTester(int steps, LongIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, LongIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<LongIterator>> getStimulusValues() {
	    List<Stimulus<LongIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<LongIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<LongIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract LongListIterator newTargetIterator();
}
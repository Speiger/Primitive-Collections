package speiger.src.testers.chars.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharListIterator;

public abstract class CharListIteratorTester extends AbstractCharIteratorTester {

	public CharListIteratorTester(int steps, CharIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, CharIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<CharIterator>> getStimulusValues() {
	    List<Stimulus<CharIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<CharIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<CharIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract CharListIterator newTargetIterator();
}
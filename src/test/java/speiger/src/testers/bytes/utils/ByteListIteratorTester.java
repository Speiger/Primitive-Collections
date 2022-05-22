package speiger.src.testers.bytes.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteListIterator;

public abstract class ByteListIteratorTester extends AbstractByteIteratorTester {

	public ByteListIteratorTester(int steps, ByteIterable elementsToInsertIterable, Iterable<? extends IteratorFeature> features, ByteIterable expectedElements, int startIndex) {
		super(steps, elementsToInsertIterable, features, expectedElements, KnownOrder.KNOWN_ORDER, startIndex);
	}

	@Override
	protected Iterable<? extends Stimulus<ByteIterator>> getStimulusValues() {
	    List<Stimulus<ByteIterator>> list = new ArrayList<>();
	    Helpers.addAll(list, iteratorStimuli());
	    for(Stimulus<?> iter : biIteratorStimuli())
	    {
	    	list.add((Stimulus<ByteIterator>)iter);
	    }
	    for(Stimulus<?> iter : listIteratorStimuli())
	    {
	    	list.add((Stimulus<ByteIterator>)iter);
	    }
	    return list;
	}

	@Override
	protected abstract ByteListIterator newTargetIterator();
}
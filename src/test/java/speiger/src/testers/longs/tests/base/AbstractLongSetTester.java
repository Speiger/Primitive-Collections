package speiger.src.testers.longs.tests.base;

import speiger.src.collections.longs.sets.LongSet;

public class AbstractLongSetTester extends AbstractLongCollectionTester {
	protected final LongSet getSet() {
		return (LongSet)collection;
	}
}
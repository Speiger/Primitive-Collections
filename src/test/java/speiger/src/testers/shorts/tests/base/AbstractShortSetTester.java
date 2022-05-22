package speiger.src.testers.shorts.tests.base;

import speiger.src.collections.shorts.sets.ShortSet;

public class AbstractShortSetTester extends AbstractShortCollectionTester {
	protected final ShortSet getSet() {
		return (ShortSet)collection;
	}
}
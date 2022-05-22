package speiger.src.testers.shorts.tests.base;

import org.junit.Ignore;

import speiger.src.collections.shorts.sets.ShortSet;

@Ignore
public class AbstractShortSetTester extends AbstractShortCollectionTester {
	protected final ShortSet getSet() {
		return (ShortSet)collection;
	}
}
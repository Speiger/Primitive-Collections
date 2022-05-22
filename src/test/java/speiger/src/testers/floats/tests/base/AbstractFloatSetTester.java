package speiger.src.testers.floats.tests.base;

import org.junit.Ignore;

import speiger.src.collections.floats.sets.FloatSet;

@Ignore
public class AbstractFloatSetTester extends AbstractFloatCollectionTester {
	protected final FloatSet getSet() {
		return (FloatSet)collection;
	}
}
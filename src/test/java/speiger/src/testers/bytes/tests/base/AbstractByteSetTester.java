package speiger.src.testers.bytes.tests.base;

import speiger.src.collections.bytes.sets.ByteSet;

public class AbstractByteSetTester extends AbstractByteCollectionTester {
	protected final ByteSet getSet() {
		return (ByteSet)collection;
	}
}
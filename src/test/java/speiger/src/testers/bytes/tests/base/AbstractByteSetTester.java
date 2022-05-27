package speiger.src.testers.bytes.tests.base;

import org.junit.Ignore;

import speiger.src.collections.bytes.sets.ByteSet;

@Ignore
public class AbstractByteSetTester extends AbstractByteCollectionTester
{
	protected final ByteSet getSet() {
		return (ByteSet)collection;
	}
}
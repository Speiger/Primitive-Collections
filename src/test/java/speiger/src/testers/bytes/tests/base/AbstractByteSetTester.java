package speiger.src.testers.bytes.tests.base;

import org.junit.Ignore;

import speiger.src.collections.bytes.sets.ByteSet;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByteSetTester extends AbstractByteCollectionTester
{
	protected final ByteSet getSet() {
		return (ByteSet)collection;
	}
}
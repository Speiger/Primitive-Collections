package speiger.src.testers.bytes.tests.base;

import org.junit.Ignore;

import speiger.src.collections.bytes.collections.ByteCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByteCollectionTester extends AbstractByteContainerTester<ByteCollection>
{
	protected ByteCollection collection;

	@Override
	protected ByteCollection actualContents() {
		return collection;
	}

	@Override
	protected ByteCollection resetContainer(ByteCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}
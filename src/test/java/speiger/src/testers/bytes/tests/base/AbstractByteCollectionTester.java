package speiger.src.testers.bytes.tests.base;

import org.junit.Ignore;

import speiger.src.collections.bytes.collections.ByteCollection;

@Ignore
public class AbstractByteCollectionTester extends AbstractByteContainerTester {

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
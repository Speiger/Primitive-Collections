package speiger.src.testers.ints.tests.base;

import org.junit.Ignore;

import speiger.src.collections.ints.collections.IntCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractIntCollectionTester extends AbstractIntContainerTester<IntCollection>
{
	protected IntCollection collection;

	@Override
	protected IntCollection actualContents() {
		return collection;
	}

	@Override
	protected IntCollection resetContainer(IntCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}
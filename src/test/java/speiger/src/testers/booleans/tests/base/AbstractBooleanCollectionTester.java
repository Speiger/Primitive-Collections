package speiger.src.testers.booleans.tests.base;

import org.junit.Ignore;

import speiger.src.collections.booleans.collections.BooleanCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractBooleanCollectionTester extends AbstractBooleanContainerTester<BooleanCollection>
{
	protected BooleanCollection collection;

	@Override
	protected BooleanCollection actualContents() {
		return collection;
	}

	@Override
	protected BooleanCollection resetContainer(BooleanCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}
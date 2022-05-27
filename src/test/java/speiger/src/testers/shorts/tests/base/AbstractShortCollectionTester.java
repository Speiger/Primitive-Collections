package speiger.src.testers.shorts.tests.base;

import org.junit.Ignore;

import speiger.src.collections.shorts.collections.ShortCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractShortCollectionTester extends AbstractShortContainerTester<ShortCollection>
{
	protected ShortCollection collection;

	@Override
	protected ShortCollection actualContents() {
		return collection;
	}

	@Override
	protected ShortCollection resetContainer(ShortCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}
package speiger.src.testers.floats.tests.base;

import org.junit.Ignore;

import speiger.src.collections.floats.collections.FloatCollection;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractFloatCollectionTester extends AbstractFloatContainerTester<FloatCollection>
{
	protected FloatCollection collection;

	@Override
	protected FloatCollection actualContents() {
		return collection;
	}

	@Override
	protected FloatCollection resetContainer(FloatCollection newContents) {
		collection = super.resetContainer(newContents);
		return collection;
	}

	protected void resetCollection() {
		resetContainer();
	}

}
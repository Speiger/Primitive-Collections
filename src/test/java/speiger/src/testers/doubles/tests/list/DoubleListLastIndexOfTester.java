package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.REJECTS_DUPLICATES_AT_CREATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.AbstractDoubleListIndexOfTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleListLastIndexOfTester extends AbstractDoubleListIndexOfTester
{
	@Override
	protected int find(double o) {
		return getList().lastIndexOf(o);
	}

	@Override
	protected String getMethodName() {
		return "lastIndexOf";
	}

	@CollectionFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testLastIndexOf_duplicate() {
		double[] array = createSamplesArray();
		array[getNumElements() / 2] = e0();
		collection = primitiveGenerator.create(array);
		assertEquals("lastIndexOf(duplicate) should return index of last occurrence", getNumElements() / 2, getList().lastIndexOf(e0()));
	}
}
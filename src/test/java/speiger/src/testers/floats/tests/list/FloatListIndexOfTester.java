package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.REJECTS_DUPLICATES_AT_CREATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.AbstractFloatListIndexOfTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatListIndexOfTester extends AbstractFloatListIndexOfTester
{
	@Override
	protected int find(float o) {
		return getList().indexOf(o);
	}

	@Override
	protected String getMethodName() {
		return "indexOf";
	}

	@CollectionFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testIndexOf_duplicate() {
		float[] array = createSamplesArray();
		array[getNumElements() / 2] = e0();
		collection = primitiveGenerator.create(array);
		assertEquals("indexOf(duplicate) should return index of first occurrence", 0, getList().indexOf(e0()));
	}
}
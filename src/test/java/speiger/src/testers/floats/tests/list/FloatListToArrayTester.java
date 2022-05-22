package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.testers.floats.tests.base.AbstractFloatListTester;

@Ignore
public class FloatListToArrayTester extends AbstractFloatListTester {
	public void testToArray_noArg() {
		float[] actual = getList().toFloatArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		float[] actual = getList().toFloatArray(new float[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		float[] actual = getList().toFloatArray(new float[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, float[] expected, float[] actual) {
		assertEquals(message, FloatArrayList.wrap(expected), FloatArrayList.wrap(actual));
	}
}
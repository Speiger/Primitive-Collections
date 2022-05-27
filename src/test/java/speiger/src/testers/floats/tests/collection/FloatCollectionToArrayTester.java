package speiger.src.testers.floats.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;
import speiger.src.testers.floats.utils.FloatHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class FloatCollectionToArrayTester extends AbstractFloatCollectionTester
{
	public void testToArray_noArgs() {
		float[] array = collection.toFloatArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		float[] empty = emptyArray();
		float[] array = collection.toFloatArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		float[] empty = emptyArray();
		float[] array = collection.toFloatArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		float[] in = emptyArray();
		float[] array = collection.toFloatArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		float[] array = new float[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toFloatArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		float[] array = new float[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toFloatArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		float[] array = new float[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toFloatArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		float[] array = new float[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toFloatArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		float[] array = new float[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toFloatArray(array));

		FloatList subArray = FloatArrayList.wrap(array).subList(0, getNumElements());
		float[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0F, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		float[] array = new float[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toFloatArray(array));

		FloatList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getFloat(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0F, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(float[] expected, float[] actual) {
		FloatHelpers.assertEqualIgnoringOrder(FloatArrayList.wrap(expected), FloatArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(FloatList expected, float[] actual) {
		assertEquals("toFloatArray() ordered contents: ", expected, FloatArrayList.wrap(actual));
	}
}
package speiger.src.testers.doubles.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
public class DoubleCollectionToArrayTester extends AbstractDoubleCollectionTester
{
	public void testToArray_noArgs() {
		double[] array = collection.toDoubleArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		double[] empty = emptyArray();
		double[] array = collection.toDoubleArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		double[] empty = emptyArray();
		double[] array = collection.toDoubleArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		double[] in = emptyArray();
		double[] array = collection.toDoubleArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		double[] array = new double[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toDoubleArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		double[] array = new double[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toDoubleArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		double[] array = new double[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toDoubleArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		double[] array = new double[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toDoubleArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		double[] array = new double[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toDoubleArray(array));

		DoubleList subArray = DoubleArrayList.wrap(array).subList(0, getNumElements());
		double[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0D, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		double[] array = new double[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toDoubleArray(array));

		DoubleList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getDouble(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", 0D, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(double[] expected, double[] actual) {
		DoubleHelpers.assertEqualIgnoringOrder(DoubleArrayList.wrap(expected), DoubleArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(DoubleList expected, double[] actual) {
		assertEquals("toDoubleArray() ordered contents: ", expected, DoubleArrayList.wrap(actual));
	}
}
package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_REMOVE_WITH_INDEX;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
public class DoubleListRemoveAtIndexTester extends AbstractDoubleListTester
{
	@ListFeature.Require(absent = SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_unsupported() {
		try {
			getList().removeDouble(0);
			fail("remove(i) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_negative() {
		try {
			getList().removeDouble(-1);
			fail("remove(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_tooLarge() {
		try {
			getList().removeDouble(getNumElements());
			fail("remove(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_first() {
		runRemoveTest(0);
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRemoveAtIndex_middle() {
		runRemoveTest(getNumElements() / 2);
	}

	@CollectionFeature.Require(FAILS_FAST_ON_CONCURRENT_MODIFICATION)
	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndexConcurrentWithIteration() {
		try {
			DoubleIterator iterator = collection.iterator();
			getList().removeDouble(getNumElements() / 2);
			iterator.nextDouble();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_last() {
		runRemoveTest(getNumElements() - 1);
	}

	private void runRemoveTest(int index) {
		assertEquals(String.format("remove(%d) should return the element at index %d", index, index), getList().getDouble(index), getList().removeDouble(index));
		DoubleList expected = DoubleHelpers.copyToList(createSamplesArray());
		expected.removeDouble(index);
		expectContents(expected);
	}
}
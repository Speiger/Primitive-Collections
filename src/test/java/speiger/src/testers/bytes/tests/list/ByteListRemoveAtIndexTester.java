package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_REMOVE_WITH_INDEX;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ByteListRemoveAtIndexTester extends AbstractByteListTester
{
	@ListFeature.Require(absent = SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_unsupported() {
		try {
			getList().removeByte(0);
			fail("remove(i) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_negative() {
		try {
			getList().removeByte(-1);
			fail("remove(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_tooLarge() {
		try {
			getList().removeByte(getNumElements());
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
			ByteIterator iterator = collection.iterator();
			getList().removeByte(getNumElements() / 2);
			iterator.nextByte();
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
		assertEquals(String.format("remove(%d) should return the element at index %d", index, index), getList().getByte(index), getList().removeByte(index));
		ByteList expected = ByteHelpers.copyToList(createSamplesArray());
		expected.removeByte(index);
		expectContents(expected);
	}
}
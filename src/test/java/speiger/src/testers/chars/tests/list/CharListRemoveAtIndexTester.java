package speiger.src.testers.chars.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_REMOVE_WITH_INDEX;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharListTester;
import speiger.src.testers.chars.utils.CharHelpers;

@Ignore
public class CharListRemoveAtIndexTester extends AbstractCharListTester {
	@ListFeature.Require(absent = SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_unsupported() {
		try {
			getList().removeChar(0);
			fail("remove(i) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_negative() {
		try {
			getList().removeChar(-1);
			fail("remove(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_tooLarge() {
		try {
			getList().removeChar(getNumElements());
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
			CharIterator iterator = collection.iterator();
			getList().removeChar(getNumElements() / 2);
			iterator.nextChar();
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
		assertEquals(String.format("remove(%d) should return the element at index %d", index, index), getList().getChar(index), getList().removeChar(index));
		CharList expected = CharHelpers.copyToList(createSamplesArray());
		expected.removeChar(index);
		expectContents(expected);
	}
}
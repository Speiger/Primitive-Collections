package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SERIALIZABLE_INCLUDING_VIEWS;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_REMOVE_WITH_INDEX;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Arrays;
import java.util.Collections;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.testing.SerializableTester;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.utils.LongLists;
import speiger.src.testers.longs.tests.base.AbstractLongListTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class LongListSubListTester extends AbstractLongListTester {
	public void testSubList_startNegative() {
		try {
			getList().subList(-1, 0);
			fail("subList(-1, 0) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testSubList_endTooLarge() {
		try {
			getList().subList(0, getNumElements() + 1);
			fail("subList(0, size + 1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testSubList_startGreaterThanEnd() {
		try {
			getList().subList(1, 0);
			fail("subList(1, 0) should throw");
		} catch (IndexOutOfBoundsException expected) {
		} catch (IllegalArgumentException expected) {
		}
	}

	public void testSubList_empty() {
		assertEquals("subList(0, 0) should be empty", LongLists.empty(), getList().subList(0, 0));
	}

	public void testSubList_entireList() {
		assertEquals("subList(0, size) should be equal to the original list", getList(), getList().subList(0, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListRemoveAffectsOriginal() {
		LongList subList = getList().subList(0, 1);
		subList.removeLong(0);
		expectContents(LongArrayList.wrap(createSamplesArray()).subList(1, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListClearAffectsOriginal() {
		LongList subList = getList().subList(0, 1);
		subList.clear();
		expectContents(LongArrayList.wrap(createSamplesArray()).subList(1, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testSubList_subListAddAffectsOriginal() {
		LongList subList = getList().subList(0, 0);
		subList.add(e3());
		expectAddedIndex(0, e3());
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListSetAffectsOriginal() {
		LongList subList = getList().subList(0, 1);
		subList.set(0, e3());
		LongList expected = LongHelpers.copyToList(createSamplesArray());
		expected.set(0, e3());
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_originalListSetAffectsSubList() {
		LongList subList = getList().subList(0, 1);
		getList().set(0, e3());
		assertEquals("A set() call to a list after a sublist has been created should be reflected in the sublist", LongLists.singleton(e3()), subList);
	}
	
	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListRemoveAffectsOriginalLargeList() {
		LongList subList = getList().subList(1, 3);
		subList.remLong(e2());
		LongList expected = LongHelpers.copyToList(createSamplesArray());
		expected.removeLong(2);
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListAddAtIndexAffectsOriginalLargeList() {
		LongList subList = getList().subList(2, 3);
		subList.add(0, e3());
		expectAddedIndex(2, e3());
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListSetAffectsOriginalLargeList() {
		LongList subList = getList().subList(1, 2);
		subList.set(0, e3());
		LongList expected = LongHelpers.copyToList(createSamplesArray());
		expected.set(1, e3());
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_originalListSetAffectsSubListLargeList() {
		LongList subList = getList().subList(1, 3);
		getList().set(1, e3());
		assertEquals("A set() call to a list after a sublist has been created " + "should be reflected in the sublist",
				Arrays.asList(e3(), e2()), subList);
	}

	public void testSubList_ofSubListEmpty() {
		LongList subList = getList().subList(0, 0).subList(0, 0);
		assertEquals("subList(0, 0).subList(0, 0) should be an empty list", LongLists.empty(), subList);
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_ofSubListNonEmpty() {
		LongList subList = getList().subList(0, 2).subList(1, 2);
		assertEquals("subList(0, 2).subList(1, 2) " + "should be a single-element list of the element at index 1",
				Collections.singletonList(getOrderedElements().getLong(1)), subList);
	}

	@CollectionSize.Require(absent = { ZERO })
	public void testSubList_size() {
		LongList list = getList();
		int size = getNumElements();
		assertEquals(size, list.subList(0, size).size());
		assertEquals(size - 1, list.subList(0, size - 1).size());
		assertEquals(size - 1, list.subList(1, size).size());
		assertEquals(0, list.subList(size, size).size());
		assertEquals(0, list.subList(0, 0).size());
	}

	@CollectionSize.Require(absent = { ZERO })
	public void testSubList_isEmpty() {
		LongList list = getList();
		int size = getNumElements();
		for (LongList subList : Arrays.asList(list.subList(0, size), list.subList(0, size - 1), list.subList(1, size),
				list.subList(0, 0), list.subList(size, size))) {
			assertEquals(subList.size() == 0, subList.isEmpty());
		}
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_get() {
		LongList list = getList();
		int size = getNumElements();
		LongList copy = list.subList(0, size);
		LongList head = list.subList(0, size - 1);
		LongList tail = list.subList(1, size);
		assertEquals(list.getLong(0), copy.getLong(0));
		assertEquals(list.getLong(size - 1), copy.getLong(size - 1));
		assertEquals(list.getLong(1), tail.getLong(0));
		assertEquals(list.getLong(size - 1), tail.getLong(size - 2));
		assertEquals(list.getLong(0), head.getLong(0));
		assertEquals(list.getLong(size - 2), head.getLong(size - 2));
		for (LongList subList : Arrays.asList(copy, head, tail)) {
			for (int index : Arrays.asList(-1, subList.size())) {
				try {
					subList.getLong(index);
					fail("expected IndexOutOfBoundsException");
				} catch (IndexOutOfBoundsException expected) {
				}
			}
		}
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_contains() {
		LongList list = getList();
		int size = getNumElements();
		LongList copy = list.subList(0, size);
		LongList head = list.subList(0, size - 1);
		LongList tail = list.subList(1, size);
		assertTrue(copy.contains(list.getLong(0)));
		assertTrue(head.contains(list.getLong(0)));
		assertTrue(tail.contains(list.getLong(1)));
		// The following assumes all elements are distinct.
		assertTrue(copy.contains(list.getLong(size - 1)));
		assertTrue(head.contains(list.getLong(size - 2)));
		assertTrue(tail.contains(list.getLong(size - 1)));
		assertFalse(head.contains(list.getLong(size - 1)));
		assertFalse(tail.contains(list.getLong(0)));
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_indexOf() {
		LongList list = getList();
		int size = getNumElements();
		LongList copy = list.subList(0, size);
		LongList head = list.subList(0, size - 1);
		LongList tail = list.subList(1, size);
		assertEquals(0, copy.indexOf(list.getLong(0)));
		assertEquals(0, head.indexOf(list.getLong(0)));
		assertEquals(0, tail.indexOf(list.getLong(1)));
		// The following assumes all elements are distinct.
		assertEquals(size - 1, copy.indexOf(list.getLong(size - 1)));
		assertEquals(size - 2, head.indexOf(list.getLong(size - 2)));
		assertEquals(size - 2, tail.indexOf(list.getLong(size - 1)));
		assertEquals(-1, head.indexOf(list.getLong(size - 1)));
		assertEquals(-1, tail.indexOf(list.getLong(0)));
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_lastIndexOf() {
		LongList list = getList();
		int size = list.size();
		LongList copy = list.subList(0, size);
		LongList head = list.subList(0, size - 1);
		LongList tail = list.subList(1, size);
		assertEquals(size - 1, copy.lastIndexOf(list.getLong(size - 1)));
		assertEquals(size - 2, head.lastIndexOf(list.getLong(size - 2)));
		assertEquals(size - 2, tail.lastIndexOf(list.getLong(size - 1)));
		// The following assumes all elements are distinct.
		assertEquals(0, copy.lastIndexOf(list.getLong(0)));
		assertEquals(0, head.lastIndexOf(list.getLong(0)));
		assertEquals(0, tail.lastIndexOf(list.getLong(1)));
		assertEquals(-1, head.lastIndexOf(list.getLong(size - 1)));
		assertEquals(-1, tail.lastIndexOf(list.getLong(0)));
	}

	@CollectionFeature.Require(SERIALIZABLE_INCLUDING_VIEWS)
	public void testReserializeWholeSubList() {
		SerializableTester.reserializeAndAssert(getList().subList(0, getNumElements()));
	}

	@CollectionFeature.Require(SERIALIZABLE_INCLUDING_VIEWS)
	public void testReserializeEmptySubList() {
		SerializableTester.reserializeAndAssert(getList().subList(0, 0));
	}

	@CollectionFeature.Require(SERIALIZABLE_INCLUDING_VIEWS)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testReserializeSubList() {
		SerializableTester.reserializeAndAssert(getList().subList(0, 2));
	}
}
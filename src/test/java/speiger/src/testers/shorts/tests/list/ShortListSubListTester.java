package speiger.src.testers.shorts.tests.list;

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

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.utils.ShortLists;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ShortListSubListTester extends AbstractShortListTester
{
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
		assertEquals("subList(0, 0) should be empty", ShortLists.empty(), getList().subList(0, 0));
	}

	public void testSubList_entireList() {
		assertEquals("subList(0, size) should be equal to the original list", getList(), getList().subList(0, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListRemoveAffectsOriginal() {
		ShortList subList = getList().subList(0, 1);
		subList.removeShort(0);
		expectContents(ShortArrayList.wrap(createSamplesArray()).subList(1, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListClearAffectsOriginal() {
		ShortList subList = getList().subList(0, 1);
		subList.clear();
		expectContents(ShortArrayList.wrap(createSamplesArray()).subList(1, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testSubList_subListAddAffectsOriginal() {
		ShortList subList = getList().subList(0, 0);
		subList.add(e3());
		expectAddedIndex(0, e3());
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListSetAffectsOriginal() {
		ShortList subList = getList().subList(0, 1);
		subList.set(0, e3());
		ShortList expected = ShortHelpers.copyToList(createSamplesArray());
		expected.set(0, e3());
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_originalListSetAffectsSubList() {
		ShortList subList = getList().subList(0, 1);
		getList().set(0, e3());
		assertEquals("A set() call to a list after a sublist has been created should be reflected in the sublist", ShortLists.singleton(e3()), subList);
	}
	
	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListRemoveAffectsOriginalLargeList() {
		ShortList subList = getList().subList(1, 3);
		subList.remShort(e2());
		ShortList expected = ShortHelpers.copyToList(createSamplesArray());
		expected.removeShort(2);
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListAddAtIndexAffectsOriginalLargeList() {
		ShortList subList = getList().subList(2, 3);
		subList.add(0, e3());
		expectAddedIndex(2, e3());
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListSetAffectsOriginalLargeList() {
		ShortList subList = getList().subList(1, 2);
		subList.set(0, e3());
		ShortList expected = ShortHelpers.copyToList(createSamplesArray());
		expected.set(1, e3());
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_originalListSetAffectsSubListLargeList() {
		ShortList subList = getList().subList(1, 3);
		getList().set(1, e3());
		assertEquals("A set() call to a list after a sublist has been created " + "should be reflected in the sublist",
				Arrays.asList(e3(), e2()), subList);
	}

	public void testSubList_ofSubListEmpty() {
		ShortList subList = getList().subList(0, 0).subList(0, 0);
		assertEquals("subList(0, 0).subList(0, 0) should be an empty list", ShortLists.empty(), subList);
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_ofSubListNonEmpty() {
		ShortList subList = getList().subList(0, 2).subList(1, 2);
		assertEquals("subList(0, 2).subList(1, 2) " + "should be a single-element list of the element at index 1",
				Collections.singletonList(getOrderedElements().getShort(1)), subList);
	}

	@CollectionSize.Require(absent = { ZERO })
	public void testSubList_size() {
		ShortList list = getList();
		int size = getNumElements();
		assertEquals(size, list.subList(0, size).size());
		assertEquals(size - 1, list.subList(0, size - 1).size());
		assertEquals(size - 1, list.subList(1, size).size());
		assertEquals(0, list.subList(size, size).size());
		assertEquals(0, list.subList(0, 0).size());
	}

	@CollectionSize.Require(absent = { ZERO })
	public void testSubList_isEmpty() {
		ShortList list = getList();
		int size = getNumElements();
		for (ShortList subList : Arrays.asList(list.subList(0, size), list.subList(0, size - 1), list.subList(1, size),
				list.subList(0, 0), list.subList(size, size))) {
			assertEquals(subList.size() == 0, subList.isEmpty());
		}
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_get() {
		ShortList list = getList();
		int size = getNumElements();
		ShortList copy = list.subList(0, size);
		ShortList head = list.subList(0, size - 1);
		ShortList tail = list.subList(1, size);
		assertEquals(list.getShort(0), copy.getShort(0));
		assertEquals(list.getShort(size - 1), copy.getShort(size - 1));
		assertEquals(list.getShort(1), tail.getShort(0));
		assertEquals(list.getShort(size - 1), tail.getShort(size - 2));
		assertEquals(list.getShort(0), head.getShort(0));
		assertEquals(list.getShort(size - 2), head.getShort(size - 2));
		for (ShortList subList : Arrays.asList(copy, head, tail)) {
			for (int index : Arrays.asList(-1, subList.size())) {
				try {
					subList.getShort(index);
					fail("expected IndexOutOfBoundsException");
				} catch (IndexOutOfBoundsException expected) {
				}
			}
		}
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_contains() {
		ShortList list = getList();
		int size = getNumElements();
		ShortList copy = list.subList(0, size);
		ShortList head = list.subList(0, size - 1);
		ShortList tail = list.subList(1, size);
		assertTrue(copy.contains(list.getShort(0)));
		assertTrue(head.contains(list.getShort(0)));
		assertTrue(tail.contains(list.getShort(1)));
		// The following assumes all elements are distinct.
		assertTrue(copy.contains(list.getShort(size - 1)));
		assertTrue(head.contains(list.getShort(size - 2)));
		assertTrue(tail.contains(list.getShort(size - 1)));
		assertFalse(head.contains(list.getShort(size - 1)));
		assertFalse(tail.contains(list.getShort(0)));
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_indexOf() {
		ShortList list = getList();
		int size = getNumElements();
		ShortList copy = list.subList(0, size);
		ShortList head = list.subList(0, size - 1);
		ShortList tail = list.subList(1, size);
		assertEquals(0, copy.indexOf(list.getShort(0)));
		assertEquals(0, head.indexOf(list.getShort(0)));
		assertEquals(0, tail.indexOf(list.getShort(1)));
		// The following assumes all elements are distinct.
		assertEquals(size - 1, copy.indexOf(list.getShort(size - 1)));
		assertEquals(size - 2, head.indexOf(list.getShort(size - 2)));
		assertEquals(size - 2, tail.indexOf(list.getShort(size - 1)));
		assertEquals(-1, head.indexOf(list.getShort(size - 1)));
		assertEquals(-1, tail.indexOf(list.getShort(0)));
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_lastIndexOf() {
		ShortList list = getList();
		int size = list.size();
		ShortList copy = list.subList(0, size);
		ShortList head = list.subList(0, size - 1);
		ShortList tail = list.subList(1, size);
		assertEquals(size - 1, copy.lastIndexOf(list.getShort(size - 1)));
		assertEquals(size - 2, head.lastIndexOf(list.getShort(size - 2)));
		assertEquals(size - 2, tail.lastIndexOf(list.getShort(size - 1)));
		// The following assumes all elements are distinct.
		assertEquals(0, copy.lastIndexOf(list.getShort(0)));
		assertEquals(0, head.lastIndexOf(list.getShort(0)));
		assertEquals(0, tail.lastIndexOf(list.getShort(1)));
		assertEquals(-1, head.lastIndexOf(list.getShort(size - 1)));
		assertEquals(-1, tail.lastIndexOf(list.getShort(0)));
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
package speiger.src.testers.ints.tests.list;

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

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.testers.ints.tests.base.AbstractIntListTester;
import speiger.src.testers.ints.utils.IntHelpers;

@Ignore
public class IntListSubListTester extends AbstractIntListTester
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
		assertEquals("subList(0, 0) should be empty", IntLists.empty(), getList().subList(0, 0));
	}

	public void testSubList_entireList() {
		assertEquals("subList(0, size) should be equal to the original list", getList(), getList().subList(0, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListRemoveAffectsOriginal() {
		IntList subList = getList().subList(0, 1);
		subList.removeInt(0);
		expectContents(IntArrayList.wrap(createSamplesArray()).subList(1, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListClearAffectsOriginal() {
		IntList subList = getList().subList(0, 1);
		subList.clear();
		expectContents(IntArrayList.wrap(createSamplesArray()).subList(1, getNumElements()));
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	public void testSubList_subListAddAffectsOriginal() {
		IntList subList = getList().subList(0, 0);
		subList.add(e3());
		expectAddedIndex(0, e3());
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_subListSetAffectsOriginal() {
		IntList subList = getList().subList(0, 1);
		subList.set(0, e3());
		IntList expected = IntHelpers.copyToList(createSamplesArray());
		expected.set(0, e3());
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = ZERO)
	public void testSubList_originalListSetAffectsSubList() {
		IntList subList = getList().subList(0, 1);
		getList().set(0, e3());
		assertEquals("A set() call to a list after a sublist has been created should be reflected in the sublist", IntLists.singleton(e3()), subList);
	}
	
	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListRemoveAffectsOriginalLargeList() {
		IntList subList = getList().subList(1, 3);
		subList.remInt(e2());
		IntList expected = IntHelpers.copyToList(createSamplesArray());
		expected.removeInt(2);
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_ADD_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListAddAtIndexAffectsOriginalLargeList() {
		IntList subList = getList().subList(2, 3);
		subList.add(0, e3());
		expectAddedIndex(2, e3());
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_subListSetAffectsOriginalLargeList() {
		IntList subList = getList().subList(1, 2);
		subList.set(0, e3());
		IntList expected = IntHelpers.copyToList(createSamplesArray());
		expected.set(1, e3());
		expectContents(expected);
	}

	@ListFeature.Require(SUPPORTS_SET)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_originalListSetAffectsSubListLargeList() {
		IntList subList = getList().subList(1, 3);
		getList().set(1, e3());
		assertEquals("A set() call to a list after a sublist has been created " + "should be reflected in the sublist",
				Arrays.asList(e3(), e2()), subList);
	}

	public void testSubList_ofSubListEmpty() {
		IntList subList = getList().subList(0, 0).subList(0, 0);
		assertEquals("subList(0, 0).subList(0, 0) should be an empty list", IntLists.empty(), subList);
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_ofSubListNonEmpty() {
		IntList subList = getList().subList(0, 2).subList(1, 2);
		assertEquals("subList(0, 2).subList(1, 2) " + "should be a single-element list of the element at index 1",
				Collections.singletonList(getOrderedElements().getInt(1)), subList);
	}

	@CollectionSize.Require(absent = { ZERO })
	public void testSubList_size() {
		IntList list = getList();
		int size = getNumElements();
		assertEquals(size, list.subList(0, size).size());
		assertEquals(size - 1, list.subList(0, size - 1).size());
		assertEquals(size - 1, list.subList(1, size).size());
		assertEquals(0, list.subList(size, size).size());
		assertEquals(0, list.subList(0, 0).size());
	}

	@CollectionSize.Require(absent = { ZERO })
	public void testSubList_isEmpty() {
		IntList list = getList();
		int size = getNumElements();
		for (IntList subList : Arrays.asList(list.subList(0, size), list.subList(0, size - 1), list.subList(1, size),
				list.subList(0, 0), list.subList(size, size))) {
			assertEquals(subList.size() == 0, subList.isEmpty());
		}
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_get() {
		IntList list = getList();
		int size = getNumElements();
		IntList copy = list.subList(0, size);
		IntList head = list.subList(0, size - 1);
		IntList tail = list.subList(1, size);
		assertEquals(list.getInt(0), copy.getInt(0));
		assertEquals(list.getInt(size - 1), copy.getInt(size - 1));
		assertEquals(list.getInt(1), tail.getInt(0));
		assertEquals(list.getInt(size - 1), tail.getInt(size - 2));
		assertEquals(list.getInt(0), head.getInt(0));
		assertEquals(list.getInt(size - 2), head.getInt(size - 2));
		for (IntList subList : Arrays.asList(copy, head, tail)) {
			for (int index : Arrays.asList(-1, subList.size())) {
				try {
					subList.getInt(index);
					fail("expected IndexOutOfBoundsException");
				} catch (IndexOutOfBoundsException expected) {
				}
			}
		}
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_contains() {
		IntList list = getList();
		int size = getNumElements();
		IntList copy = list.subList(0, size);
		IntList head = list.subList(0, size - 1);
		IntList tail = list.subList(1, size);
		assertTrue(copy.contains(list.getInt(0)));
		assertTrue(head.contains(list.getInt(0)));
		assertTrue(tail.contains(list.getInt(1)));
		// The following assumes all elements are distinct.
		assertTrue(copy.contains(list.getInt(size - 1)));
		assertTrue(head.contains(list.getInt(size - 2)));
		assertTrue(tail.contains(list.getInt(size - 1)));
		assertFalse(head.contains(list.getInt(size - 1)));
		assertFalse(tail.contains(list.getInt(0)));
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_indexOf() {
		IntList list = getList();
		int size = getNumElements();
		IntList copy = list.subList(0, size);
		IntList head = list.subList(0, size - 1);
		IntList tail = list.subList(1, size);
		assertEquals(0, copy.indexOf(list.getInt(0)));
		assertEquals(0, head.indexOf(list.getInt(0)));
		assertEquals(0, tail.indexOf(list.getInt(1)));
		// The following assumes all elements are distinct.
		assertEquals(size - 1, copy.indexOf(list.getInt(size - 1)));
		assertEquals(size - 2, head.indexOf(list.getInt(size - 2)));
		assertEquals(size - 2, tail.indexOf(list.getInt(size - 1)));
		assertEquals(-1, head.indexOf(list.getInt(size - 1)));
		assertEquals(-1, tail.indexOf(list.getInt(0)));
	}

	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testSubList_lastIndexOf() {
		IntList list = getList();
		int size = list.size();
		IntList copy = list.subList(0, size);
		IntList head = list.subList(0, size - 1);
		IntList tail = list.subList(1, size);
		assertEquals(size - 1, copy.lastIndexOf(list.getInt(size - 1)));
		assertEquals(size - 2, head.lastIndexOf(list.getInt(size - 2)));
		assertEquals(size - 2, tail.lastIndexOf(list.getInt(size - 1)));
		// The following assumes all elements are distinct.
		assertEquals(0, copy.lastIndexOf(list.getInt(0)));
		assertEquals(0, head.lastIndexOf(list.getInt(0)));
		assertEquals(0, tail.lastIndexOf(list.getInt(1)));
		assertEquals(-1, head.lastIndexOf(list.getInt(size - 1)));
		assertEquals(-1, tail.lastIndexOf(list.getInt(0)));
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
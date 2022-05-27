package speiger.src.testers.booleans.utils;


import org.junit.Assert;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.booleans.utils.BooleanIterators;

public class BooleanHelpers {
	public static boolean equals(boolean key, boolean value) {
		return key == value;
	}

	public static BooleanList copyToList(BooleanIterable elements) {
		BooleanList list = new BooleanArrayList();
		addAll(list, elements);
		return list;
	}

	public static BooleanList copyToList(boolean[] elements) {
		return copyToList(BooleanArrayList.wrap(elements));
	}
	
	public static boolean addAll(BooleanCollection addTo, BooleanIterable elementsToAdd) {
		boolean modified = false;
		for (BooleanIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextBoolean());
		}
		return modified;
	}

	public static void assertEqualInOrder(BooleanIterable expected, BooleanIterable actual) {
		BooleanIterator expectedIter = expected.iterator();
		BooleanIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (expectedIter.nextBoolean() == actualIter.nextBoolean())
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(BooleanIterable actual, boolean... expected) {
		assertEqualInOrder(BooleanArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(BooleanIterable expected, BooleanIterable actual) {
		BooleanList exp = copyToList(expected);
		BooleanList act = copyToList(actual);
		String actString = act.toString();
		for (BooleanIterator iter = exp.iterator(); iter.hasNext();) {
			boolean value = iter.nextBoolean();
			if (!act.remBoolean(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(BooleanIterable actual, boolean... expected) {
		assertEqualIgnoringOrder(BooleanArrayList.wrap(expected), actual);
	}

	public static void assertContains(BooleanIterable actual, boolean expected) {
		boolean contained = false;
		if (actual instanceof BooleanCollection) {
			contained = ((BooleanCollection) actual).contains(expected);
		} else {
			for (BooleanIterator iter = actual.iterator(); iter.hasNext();) {
				if (iter.nextBoolean() == expected) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(BooleanIterable actual, boolean... expected) {
		BooleanList expectedList = new BooleanArrayList();
		expectedList.addAll(expected);

		for (BooleanIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remBoolean(iter.nextBoolean());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+BooleanArrayList.wrap(expected));
		}
	}

	static BooleanIterable reverse(BooleanList list) {
		return new BooleanIterable() {
			@Override
			public BooleanIterator iterator() {
				final BooleanListIterator listIter = list.listIterator(list.size());
				return new BooleanIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public boolean nextBoolean() {
						return listIter.previousBoolean();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static BooleanIterator cycle(BooleanIterable iterable) {
		return new BooleanIterator() {
			BooleanIterator iterator = BooleanIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public boolean nextBoolean() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextBoolean();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static boolean get(BooleanIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextBoolean();
		}
		return iterator.nextBoolean();
	}
	
	public static void testComparator(BooleanComparator comparator, boolean... valuesInExpectedOrder) {
		testComparator(comparator, BooleanArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(BooleanComparator comparator, BooleanList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			boolean t = valuesInExpectedOrder.getBoolean(i);
			for (int j = 0; j < i; j++) {
				boolean lesser = valuesInExpectedOrder.getBoolean(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				boolean greater = valuesInExpectedOrder.getBoolean(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(BooleanList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			boolean t = valuesInExpectedOrder.getBoolean(i);

			for (int j = 0; j < i; j++) {
				boolean lesser = valuesInExpectedOrder.getBoolean(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Boolean.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Boolean.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				boolean greater = valuesInExpectedOrder.getBoolean(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Boolean.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static BooleanCollection misleadingSizeCollection(final int delta) {
		return new BooleanArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
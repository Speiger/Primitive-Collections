package speiger.src.testers.ints.utils;


import org.junit.Assert;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.sets.IntLinkedOpenHashSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntIterators;

public class IntHelpers {
	private static boolean equals(int key, int value) {
		return key == value;
	}

	public static IntList copyToList(IntIterable elements) {
		IntList list = new IntArrayList();
		addAll(list, elements);
		return list;
	}

	public static IntList copyToList(int[] elements) {
		return copyToList(IntArrayList.wrap(elements));
	}
	
	public static IntSet copyToSet(IntIterable elements) {
		IntSet set = new IntLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static IntSet copyToSet(int[] elements) {
		return copyToSet(IntArrayList.wrap(elements));
	}

	public static boolean addAll(IntCollection addTo, IntIterable elementsToAdd) {
		boolean modified = false;
		for (IntIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextInt());
		}
		return modified;
	}

	public static void assertEqualInOrder(IntIterable expected, IntIterable actual) {
		IntIterator expectedIter = expected.iterator();
		IntIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (expectedIter.nextInt() == actualIter.nextInt())
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(IntIterable actual, int... expected) {
		assertEqualInOrder(IntArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(IntIterable expected, IntIterable actual) {
		IntList exp = copyToList(expected);
		IntList act = copyToList(actual);
		String actString = act.toString();
		for (IntIterator iter = exp.iterator(); iter.hasNext();) {
			int value = iter.nextInt();
			if (!act.remInt(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(IntIterable actual, int... expected) {
		assertEqualIgnoringOrder(IntArrayList.wrap(expected), actual);
	}

	public static void assertContains(IntIterable actual, int expected) {
		boolean contained = false;
		if (actual instanceof IntCollection) {
			contained = ((IntCollection) actual).contains(expected);
		} else {
			for (IntIterator iter = actual.iterator(); iter.hasNext();) {
				if (iter.nextInt() == expected) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(IntIterable actual, int... expected) {
		IntList expectedList = new IntArrayList();
		expectedList.addAll(expected);

		for (IntIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remInt(iter.nextInt());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+IntArrayList.wrap(expected));
		}
	}

	static IntIterable reverse(IntList list) {
		return new IntIterable() {
			@Override
			public IntIterator iterator() {
				final IntListIterator listIter = list.listIterator(list.size());
				return new IntIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public int nextInt() {
						return listIter.previousInt();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static IntIterator cycle(IntIterable iterable) {
		return new IntIterator() {
			IntIterator iterator = IntIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public int nextInt() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextInt();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static int get(IntIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextInt();
		}
		return iterator.nextInt();
	}

	public static void testComparator(IntComparator comparator, int... valuesInExpectedOrder) {
		testComparator(comparator, IntArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(IntComparator comparator, IntList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			int t = valuesInExpectedOrder.getInt(i);
			for (int j = 0; j < i; j++) {
				int lesser = valuesInExpectedOrder.getInt(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				int greater = valuesInExpectedOrder.getInt(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(IntList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			int t = valuesInExpectedOrder.getInt(i);

			for (int j = 0; j < i; j++) {
				int lesser = valuesInExpectedOrder.getInt(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Integer.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Integer.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				int greater = valuesInExpectedOrder.getInt(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Integer.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static IntCollection misleadingSizeCollection(final int delta) {
		return new IntArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
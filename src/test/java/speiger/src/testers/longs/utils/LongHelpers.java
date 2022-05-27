package speiger.src.testers.longs.utils;


import org.junit.Assert;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.lists.LongListIterator;
import speiger.src.collections.longs.sets.LongLinkedOpenHashSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongIterators;

public class LongHelpers {
	public static boolean equals(long key, long value) {
		return key == value;
	}

	public static LongList copyToList(LongIterable elements) {
		LongList list = new LongArrayList();
		addAll(list, elements);
		return list;
	}

	public static LongList copyToList(long[] elements) {
		return copyToList(LongArrayList.wrap(elements));
	}
	
	public static LongSet copyToSet(LongIterable elements) {
		LongSet set = new LongLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static LongSet copyToSet(long[] elements) {
		return copyToSet(LongArrayList.wrap(elements));
	}

	public static boolean addAll(LongCollection addTo, LongIterable elementsToAdd) {
		boolean modified = false;
		for (LongIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextLong());
		}
		return modified;
	}

	public static void assertEqualInOrder(LongIterable expected, LongIterable actual) {
		LongIterator expectedIter = expected.iterator();
		LongIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (expectedIter.nextLong() == actualIter.nextLong())
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(LongIterable actual, long... expected) {
		assertEqualInOrder(LongArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(LongIterable expected, LongIterable actual) {
		LongList exp = copyToList(expected);
		LongList act = copyToList(actual);
		String actString = act.toString();
		for (LongIterator iter = exp.iterator(); iter.hasNext();) {
			long value = iter.nextLong();
			if (!act.remLong(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(LongIterable actual, long... expected) {
		assertEqualIgnoringOrder(LongArrayList.wrap(expected), actual);
	}

	public static void assertContains(LongIterable actual, long expected) {
		boolean contained = false;
		if (actual instanceof LongCollection) {
			contained = ((LongCollection) actual).contains(expected);
		} else {
			for (LongIterator iter = actual.iterator(); iter.hasNext();) {
				if (iter.nextLong() == expected) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(LongIterable actual, long... expected) {
		LongList expectedList = new LongArrayList();
		expectedList.addAll(expected);

		for (LongIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remLong(iter.nextLong());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+LongArrayList.wrap(expected));
		}
	}

	static LongIterable reverse(LongList list) {
		return new LongIterable() {
			@Override
			public LongIterator iterator() {
				final LongListIterator listIter = list.listIterator(list.size());
				return new LongIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public long nextLong() {
						return listIter.previousLong();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static LongIterator cycle(LongIterable iterable) {
		return new LongIterator() {
			LongIterator iterator = LongIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public long nextLong() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextLong();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static long get(LongIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextLong();
		}
		return iterator.nextLong();
	}
	
	public static void testComparator(LongComparator comparator, long... valuesInExpectedOrder) {
		testComparator(comparator, LongArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(LongComparator comparator, LongList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			long t = valuesInExpectedOrder.getLong(i);
			for (int j = 0; j < i; j++) {
				long lesser = valuesInExpectedOrder.getLong(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				long greater = valuesInExpectedOrder.getLong(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(LongList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			long t = valuesInExpectedOrder.getLong(i);

			for (int j = 0; j < i; j++) {
				long lesser = valuesInExpectedOrder.getLong(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Long.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Long.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				long greater = valuesInExpectedOrder.getLong(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Long.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static LongCollection misleadingSizeCollection(final int delta) {
		return new LongArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
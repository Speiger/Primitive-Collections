package speiger.src.testers.shorts.utils;


import org.junit.Assert;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.sets.ShortLinkedOpenHashSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortIterators;

public class ShortHelpers {
	public static boolean equals(short key, short value) {
		return key == value;
	}

	public static ShortList copyToList(ShortIterable elements) {
		ShortList list = new ShortArrayList();
		addAll(list, elements);
		return list;
	}

	public static ShortList copyToList(short[] elements) {
		return copyToList(ShortArrayList.wrap(elements));
	}
	
	public static ShortSet copyToSet(ShortIterable elements) {
		ShortSet set = new ShortLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static ShortSet copyToSet(short[] elements) {
		return copyToSet(ShortArrayList.wrap(elements));
	}

	public static boolean addAll(ShortCollection addTo, ShortIterable elementsToAdd) {
		boolean modified = false;
		for (ShortIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextShort());
		}
		return modified;
	}

	public static void assertEqualInOrder(ShortIterable expected, ShortIterable actual) {
		ShortIterator expectedIter = expected.iterator();
		ShortIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (expectedIter.nextShort() == actualIter.nextShort())
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(ShortIterable actual, short... expected) {
		assertEqualInOrder(ShortArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(ShortIterable expected, ShortIterable actual) {
		ShortList exp = copyToList(expected);
		ShortList act = copyToList(actual);
		String actString = act.toString();
		for (ShortIterator iter = exp.iterator(); iter.hasNext();) {
			short value = iter.nextShort();
			if (!act.remShort(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(ShortIterable actual, short... expected) {
		assertEqualIgnoringOrder(ShortArrayList.wrap(expected), actual);
	}

	public static void assertContains(ShortIterable actual, short expected) {
		boolean contained = false;
		if (actual instanceof ShortCollection) {
			contained = ((ShortCollection) actual).contains(expected);
		} else {
			for (ShortIterator iter = actual.iterator(); iter.hasNext();) {
				if (iter.nextShort() == expected) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(ShortIterable actual, short... expected) {
		ShortList expectedList = new ShortArrayList();
		expectedList.addAll(expected);

		for (ShortIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remShort(iter.nextShort());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+ShortArrayList.wrap(expected));
		}
	}

	static ShortIterable reverse(ShortList list) {
		return new ShortIterable() {
			@Override
			public ShortIterator iterator() {
				final ShortListIterator listIter = list.listIterator(list.size());
				return new ShortIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public short nextShort() {
						return listIter.previousShort();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static ShortIterator cycle(ShortIterable iterable) {
		return new ShortIterator() {
			ShortIterator iterator = ShortIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public short nextShort() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextShort();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static short get(ShortIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextShort();
		}
		return iterator.nextShort();
	}
	
	public static void testComparator(ShortComparator comparator, short... valuesInExpectedOrder) {
		testComparator(comparator, ShortArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(ShortComparator comparator, ShortList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			short t = valuesInExpectedOrder.getShort(i);
			for (int j = 0; j < i; j++) {
				short lesser = valuesInExpectedOrder.getShort(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				short greater = valuesInExpectedOrder.getShort(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(ShortList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			short t = valuesInExpectedOrder.getShort(i);

			for (int j = 0; j < i; j++) {
				short lesser = valuesInExpectedOrder.getShort(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Short.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Short.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				short greater = valuesInExpectedOrder.getShort(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Short.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static ShortCollection misleadingSizeCollection(final int delta) {
		return new ShortArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
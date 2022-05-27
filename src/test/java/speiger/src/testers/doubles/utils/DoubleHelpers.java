package speiger.src.testers.doubles.utils;


import org.junit.Assert;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleIterators;

public class DoubleHelpers {
	public static boolean equals(double key, double value) {
		return key == value;
	}

	public static DoubleList copyToList(DoubleIterable elements) {
		DoubleList list = new DoubleArrayList();
		addAll(list, elements);
		return list;
	}

	public static DoubleList copyToList(double[] elements) {
		return copyToList(DoubleArrayList.wrap(elements));
	}
	
	public static DoubleSet copyToSet(DoubleIterable elements) {
		DoubleSet set = new DoubleLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static DoubleSet copyToSet(double[] elements) {
		return copyToSet(DoubleArrayList.wrap(elements));
	}

	public static boolean addAll(DoubleCollection addTo, DoubleIterable elementsToAdd) {
		boolean modified = false;
		for (DoubleIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextDouble());
		}
		return modified;
	}

	public static void assertEqualInOrder(DoubleIterable expected, DoubleIterable actual) {
		DoubleIterator expectedIter = expected.iterator();
		DoubleIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (Double.doubleToLongBits(expectedIter.nextDouble()) == Double.doubleToLongBits(actualIter.nextDouble()))
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(DoubleIterable actual, double... expected) {
		assertEqualInOrder(DoubleArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(DoubleIterable expected, DoubleIterable actual) {
		DoubleList exp = copyToList(expected);
		DoubleList act = copyToList(actual);
		String actString = act.toString();
		for (DoubleIterator iter = exp.iterator(); iter.hasNext();) {
			double value = iter.nextDouble();
			if (!act.remDouble(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(DoubleIterable actual, double... expected) {
		assertEqualIgnoringOrder(DoubleArrayList.wrap(expected), actual);
	}

	public static void assertContains(DoubleIterable actual, double expected) {
		boolean contained = false;
		if (actual instanceof DoubleCollection) {
			contained = ((DoubleCollection) actual).contains(expected);
		} else {
			for (DoubleIterator iter = actual.iterator(); iter.hasNext();) {
				if (Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(expected)) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(DoubleIterable actual, double... expected) {
		DoubleList expectedList = new DoubleArrayList();
		expectedList.addAll(expected);

		for (DoubleIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remDouble(iter.nextDouble());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+DoubleArrayList.wrap(expected));
		}
	}

	static DoubleIterable reverse(DoubleList list) {
		return new DoubleIterable() {
			@Override
			public DoubleIterator iterator() {
				final DoubleListIterator listIter = list.listIterator(list.size());
				return new DoubleIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public double nextDouble() {
						return listIter.previousDouble();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static DoubleIterator cycle(DoubleIterable iterable) {
		return new DoubleIterator() {
			DoubleIterator iterator = DoubleIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public double nextDouble() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextDouble();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static double get(DoubleIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextDouble();
		}
		return iterator.nextDouble();
	}
	
	public static void testComparator(DoubleComparator comparator, double... valuesInExpectedOrder) {
		testComparator(comparator, DoubleArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(DoubleComparator comparator, DoubleList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			double t = valuesInExpectedOrder.getDouble(i);
			for (int j = 0; j < i; j++) {
				double lesser = valuesInExpectedOrder.getDouble(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				double greater = valuesInExpectedOrder.getDouble(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(DoubleList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			double t = valuesInExpectedOrder.getDouble(i);

			for (int j = 0; j < i; j++) {
				double lesser = valuesInExpectedOrder.getDouble(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Double.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Double.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				double greater = valuesInExpectedOrder.getDouble(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Double.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static DoubleCollection misleadingSizeCollection(final int delta) {
		return new DoubleArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
package speiger.src.testers.floats.utils;


import org.junit.Assert;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.lists.FloatListIterator;
import speiger.src.collections.floats.sets.FloatLinkedOpenHashSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatIterators;

@SuppressWarnings("javadoc")
public class FloatHelpers {
	public static boolean equals(float key, float value) {
		return key == value;
	}

	public static FloatList copyToList(FloatIterable elements) {
		FloatList list = new FloatArrayList();
		addAll(list, elements);
		return list;
	}

	public static FloatList copyToList(float[] elements) {
		return copyToList(FloatArrayList.wrap(elements));
	}
	
	public static FloatSet copyToSet(FloatIterable elements) {
		FloatSet set = new FloatLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static FloatSet copyToSet(float[] elements) {
		return copyToSet(FloatArrayList.wrap(elements));
	}

	public static boolean addAll(FloatCollection addTo, FloatIterable elementsToAdd) {
		boolean modified = false;
		for (FloatIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextFloat());
		}
		return modified;
	}

	public static void assertEqualInOrder(FloatIterable expected, FloatIterable actual) {
		FloatIterator expectedIter = expected.iterator();
		FloatIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (Float.floatToIntBits(expectedIter.nextFloat()) == Float.floatToIntBits(actualIter.nextFloat()))
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(FloatIterable actual, float... expected) {
		assertEqualInOrder(FloatArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(FloatIterable expected, FloatIterable actual) {
		FloatList exp = copyToList(expected);
		FloatList act = copyToList(actual);
		String actString = act.toString();
		for (FloatIterator iter = exp.iterator(); iter.hasNext();) {
			float value = iter.nextFloat();
			if (!act.remFloat(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(FloatIterable actual, float... expected) {
		assertEqualIgnoringOrder(FloatArrayList.wrap(expected), actual);
	}

	public static void assertContains(FloatIterable actual, float expected) {
		boolean contained = false;
		if (actual instanceof FloatCollection) {
			contained = ((FloatCollection) actual).contains(expected);
		} else {
			for (FloatIterator iter = actual.iterator(); iter.hasNext();) {
				if (Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(expected)) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(FloatIterable actual, float... expected) {
		FloatList expectedList = new FloatArrayList();
		expectedList.addAll(expected);

		for (FloatIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remFloat(iter.nextFloat());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+FloatArrayList.wrap(expected));
		}
	}

	static FloatIterable reverse(FloatList list) {
		return new FloatIterable() {
			@Override
			public FloatIterator iterator() {
				final FloatListIterator listIter = list.listIterator(list.size());
				return new FloatIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public float nextFloat() {
						return listIter.previousFloat();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static FloatIterator cycle(FloatIterable iterable) {
		return new FloatIterator() {
			FloatIterator iterator = FloatIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public float nextFloat() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextFloat();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static float get(FloatIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextFloat();
		}
		return iterator.nextFloat();
	}
	
	public static void testComparator(FloatComparator comparator, float... valuesInExpectedOrder) {
		testComparator(comparator, FloatArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(FloatComparator comparator, FloatList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			float t = valuesInExpectedOrder.getFloat(i);
			for (int j = 0; j < i; j++) {
				float lesser = valuesInExpectedOrder.getFloat(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				float greater = valuesInExpectedOrder.getFloat(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(FloatList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			float t = valuesInExpectedOrder.getFloat(i);

			for (int j = 0; j < i; j++) {
				float lesser = valuesInExpectedOrder.getFloat(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Float.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Float.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				float greater = valuesInExpectedOrder.getFloat(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Float.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static FloatCollection misleadingSizeCollection(final int delta) {
		return new FloatArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
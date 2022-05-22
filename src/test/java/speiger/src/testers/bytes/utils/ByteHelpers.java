package speiger.src.testers.bytes.utils;


import org.junit.Assert;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.sets.ByteLinkedOpenHashSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteIterators;

public class ByteHelpers {
	private static boolean equals(byte key, byte value) {
		return key == value;
	}

	public static ByteList copyToList(ByteIterable elements) {
		ByteList list = new ByteArrayList();
		addAll(list, elements);
		return list;
	}

	public static ByteList copyToList(byte[] elements) {
		return copyToList(ByteArrayList.wrap(elements));
	}
	
	public static ByteSet copyToSet(ByteIterable elements) {
		ByteSet set = new ByteLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static ByteSet copyToSet(byte[] elements) {
		return copyToSet(ByteArrayList.wrap(elements));
	}

	public static boolean addAll(ByteCollection addTo, ByteIterable elementsToAdd) {
		boolean modified = false;
		for (ByteIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextByte());
		}
		return modified;
	}

	public static void assertEqualInOrder(ByteIterable expected, ByteIterable actual) {
		ByteIterator expectedIter = expected.iterator();
		ByteIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (expectedIter.nextByte() == actualIter.nextByte())
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(ByteIterable actual, byte... expected) {
		assertEqualInOrder(ByteArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(ByteIterable expected, ByteIterable actual) {
		ByteList exp = copyToList(expected);
		ByteList act = copyToList(actual);
		String actString = act.toString();
		for (ByteIterator iter = exp.iterator(); iter.hasNext();) {
			byte value = iter.nextByte();
			if (!act.remByte(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(ByteIterable actual, byte... expected) {
		assertEqualIgnoringOrder(ByteArrayList.wrap(expected), actual);
	}

	public static void assertContains(ByteIterable actual, byte expected) {
		boolean contained = false;
		if (actual instanceof ByteCollection) {
			contained = ((ByteCollection) actual).contains(expected);
		} else {
			for (ByteIterator iter = actual.iterator(); iter.hasNext();) {
				if (iter.nextByte() == expected) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(ByteIterable actual, byte... expected) {
		ByteList expectedList = new ByteArrayList();
		expectedList.addAll(expected);

		for (ByteIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remByte(iter.nextByte());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+ByteArrayList.wrap(expected));
		}
	}

	static ByteIterable reverse(ByteList list) {
		return new ByteIterable() {
			@Override
			public ByteIterator iterator() {
				final ByteListIterator listIter = list.listIterator(list.size());
				return new ByteIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public byte nextByte() {
						return listIter.previousByte();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static ByteIterator cycle(ByteIterable iterable) {
		return new ByteIterator() {
			ByteIterator iterator = ByteIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public byte nextByte() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextByte();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static byte get(ByteIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextByte();
		}
		return iterator.nextByte();
	}

	public static void testComparator(ByteComparator comparator, byte... valuesInExpectedOrder) {
		testComparator(comparator, ByteArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(ByteComparator comparator, ByteList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			byte t = valuesInExpectedOrder.getByte(i);
			for (int j = 0; j < i; j++) {
				byte lesser = valuesInExpectedOrder.getByte(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				byte greater = valuesInExpectedOrder.getByte(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(ByteList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			byte t = valuesInExpectedOrder.getByte(i);

			for (int j = 0; j < i; j++) {
				byte lesser = valuesInExpectedOrder.getByte(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Byte.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Byte.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				byte greater = valuesInExpectedOrder.getByte(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Byte.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static ByteCollection misleadingSizeCollection(final int delta) {
		return new ByteArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
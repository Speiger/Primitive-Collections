package speiger.src.testers.chars.utils;


import org.junit.Assert;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.sets.CharLinkedOpenHashSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharIterators;

@SuppressWarnings("javadoc")
public class CharHelpers {
	public static boolean equals(char key, char value) {
		return key == value;
	}

	public static CharList copyToList(CharIterable elements) {
		CharList list = new CharArrayList();
		addAll(list, elements);
		return list;
	}

	public static CharList copyToList(char[] elements) {
		return copyToList(CharArrayList.wrap(elements));
	}
	
	public static CharSet copyToSet(CharIterable elements) {
		CharSet set = new CharLinkedOpenHashSet();
		addAll(set, elements);
		return set;
	}

	public static CharSet copyToSet(char[] elements) {
		return copyToSet(CharArrayList.wrap(elements));
	}

	public static boolean addAll(CharCollection addTo, CharIterable elementsToAdd) {
		boolean modified = false;
		for (CharIterator iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.nextChar());
		}
		return modified;
	}

	public static void assertEqualInOrder(CharIterable expected, CharIterable actual) {
		CharIterator expectedIter = expected.iterator();
		CharIterator actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (expectedIter.nextChar() == actualIter.nextChar())
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static void assertContentsInOrder(CharIterable actual, char... expected) {
		assertEqualInOrder(CharArrayList.wrap(expected), actual);
	}

	public static void assertEqualIgnoringOrder(CharIterable expected, CharIterable actual) {
		CharList exp = copyToList(expected);
		CharList act = copyToList(actual);
		String actString = act.toString();
		for (CharIterator iter = exp.iterator(); iter.hasNext();) {
			char value = iter.nextChar();
			if (!act.remChar(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static void assertContentsAnyOrder(CharIterable actual, char... expected) {
		assertEqualIgnoringOrder(CharArrayList.wrap(expected), actual);
	}

	public static void assertContains(CharIterable actual, char expected) {
		boolean contained = false;
		if (actual instanceof CharCollection) {
			contained = ((CharCollection) actual).contains(expected);
		} else {
			for (CharIterator iter = actual.iterator(); iter.hasNext();) {
				if (iter.nextChar() == expected) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static void assertContainsAllOf(CharIterable actual, char... expected) {
		CharList expectedList = new CharArrayList();
		expectedList.addAll(expected);

		for (CharIterator iter = actual.iterator(); iter.hasNext();) {
			expectedList.remChar(iter.nextChar());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+CharArrayList.wrap(expected));
		}
	}

	static CharIterable reverse(CharList list) {
		return new CharIterable() {
			@Override
			public CharIterator iterator() {
				final CharListIterator listIter = list.listIterator(list.size());
				return new CharIterator() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public char nextChar() {
						return listIter.previousChar();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static CharIterator cycle(CharIterable iterable) {
		return new CharIterator() {
			CharIterator iterator = CharIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public char nextChar() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.nextChar();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static char get(CharIterator iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.nextChar();
		}
		return iterator.nextChar();
	}
	
	public static void testComparator(CharComparator comparator, char... valuesInExpectedOrder) {
		testComparator(comparator, CharArrayList.wrap(valuesInExpectedOrder));
	}

	public static void testComparator(CharComparator comparator, CharList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			char t = valuesInExpectedOrder.getChar(i);
			for (int j = 0; j < i; j++) {
				char lesser = valuesInExpectedOrder.getChar(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				char greater = valuesInExpectedOrder.getChar(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static void testCompareToAndEquals(CharList valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			char t = valuesInExpectedOrder.getChar(i);

			for (int j = 0; j < i; j++) {
				char lesser = valuesInExpectedOrder.getChar(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', Character.compare(lesser, t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, Character.compare(t, t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				char greater = valuesInExpectedOrder.getChar(j);
				Assert.assertTrue(greater+".compareTo("+t+')', Character.compare(greater, t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static CharCollection misleadingSizeCollection(final int delta) {
		return new CharArrayList() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
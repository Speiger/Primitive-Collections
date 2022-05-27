package speiger.src.testers.objects.utils;

import java.util.Comparator;
import java.util.Objects;

import org.junit.Assert;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.ObjectLinkedOpenHashSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;

@SuppressWarnings("javadoc")
public class ObjectHelpers {
	public static <T> boolean equals(T key, T value) {
		return key == value;
	}

	public static <T> ObjectList<T> copyToList(ObjectIterable<? extends T> elements) {
		ObjectList<T> list = new ObjectArrayList<>();
		addAll(list, elements);
		return list;
	}

	public static <T> ObjectList<T> copyToList(T[] elements) {
		return copyToList(ObjectArrayList.wrap(elements));
	}
	
	public static <T> ObjectSet<T> copyToSet(ObjectIterable<? extends T> elements) {
		ObjectSet<T> set = new ObjectLinkedOpenHashSet<>();
		addAll(set, elements);
		return set;
	}

	public static <T> ObjectSet<T> copyToSet(T[] elements) {
		return copyToSet(ObjectArrayList.wrap(elements));
	}

	public static <T> boolean addAll(ObjectCollection<T> addTo, ObjectIterable<? extends T> elementsToAdd) {
		boolean modified = false;
		for (ObjectIterator<? extends T> iter = elementsToAdd.iterator(); iter.hasNext();) {
			modified |= addTo.add(iter.next());
		}
		return modified;
	}

	public static <T> void assertEqualInOrder(ObjectIterable<? extends T> expected, ObjectIterable<? extends T> actual) {
		ObjectIterator<? extends T> expectedIter = expected.iterator();
		ObjectIterator<? extends T> actualIter = actual.iterator();
		while (expectedIter.hasNext() && actualIter.hasNext()) {
			if (Objects.equals(expectedIter.next(), actualIter.next()))
				continue;
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
		if (expectedIter.hasNext() || actualIter.hasNext()) {
			Assert.fail("contents were not equal and in the same order: expected = "+expected+", actual = "+actual);
		}
	}

	public static <T> void assertContentsInOrder(ObjectIterable<? extends T> actual, T... expected) {
		assertEqualInOrder(ObjectArrayList.wrap(expected), actual);
	}

	public static <T> void assertEqualIgnoringOrder(ObjectIterable<? extends T> expected, ObjectIterable<? extends T> actual) {
		ObjectList<T> exp = copyToList(expected);
		ObjectList<T> act = copyToList(actual);
		String actString = act.toString();
		for (ObjectIterator<? extends T> iter = exp.iterator(); iter.hasNext();) {
			T value = iter.next();
			if (!act.remove(value)) {
				Assert.fail("did not contain expected element "+value+", expected = "+exp+", actual = "+ actString);
			}
		}
		Assert.assertTrue("unexpected elements: "+act, act.isEmpty());
	}

	public static <T> void assertContentsAnyOrder(ObjectIterable<? extends T> actual, T... expected) {
		assertEqualIgnoringOrder(ObjectArrayList.wrap(expected), actual);
	}

	public static <T> void assertContains(ObjectIterable<? extends T> actual, T expected) {
		boolean contained = false;
		if (actual instanceof ObjectCollection) {
			contained = ((ObjectCollection<T>) actual).contains(expected);
		} else {
			for (ObjectIterator<? extends T> iter = actual.iterator(); iter.hasNext();) {
				if (Objects.equals(iter.next(), expected)) {
					contained = true;
					break;
				}
			}
		}

		if (!contained) {
			Assert.fail("Not true that "+actual+" contains "+expected);
		}
	}

	public static <T> void assertContainsAllOf(ObjectIterable<? extends T> actual, T... expected) {
		ObjectList<T> expectedList = new ObjectArrayList<>();
		expectedList.addAll(expected);

		for (ObjectIterator<? extends T> iter = actual.iterator(); iter.hasNext();) {
			expectedList.remove(iter.next());
		}
		if (!expectedList.isEmpty()) {
			Assert.fail("Not true that "+actual+" contains all of "+ObjectArrayList.wrap(expected));
		}
	}

	static <T> ObjectIterable<T> reverse(ObjectList<T> list) {
		return new ObjectIterable<T>() {
			@Override
			public ObjectIterator<T> iterator() {
				final ObjectListIterator<T> listIter = list.listIterator(list.size());
				return new ObjectIterator<T>() {
					@Override
					public boolean hasNext() {
						return listIter.hasPrevious();
					}

					@Override
					public T next() {
						return listIter.previous();
					}

					@Override
					public void remove() {
						listIter.remove();
					}
				};
			}
		};
	}

	static <T> ObjectIterator<T> cycle(ObjectIterable<T> iterable) {
		return new ObjectIterator<T>() {
			ObjectIterator<T> iterator = ObjectIterators.empty();

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public T next() {
				if (!iterator.hasNext()) {
					iterator = iterable.iterator();
				}
				return iterator.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	static <T> T get(ObjectIterator<T> iterator, int position) {
		for (int i = 0; i < position; i++) {
			iterator.next();
		}
		return iterator.next();
	}
	
	public static <T> void testComparator(Comparator<T> comparator, T... valuesInExpectedOrder) {
		testComparator(comparator, ObjectArrayList.wrap(valuesInExpectedOrder));
	}

	public static <T> void testComparator(Comparator<T> comparator, ObjectList<T> valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			T t = valuesInExpectedOrder.get(i);
			for (int j = 0; j < i; j++) {
				T lesser = valuesInExpectedOrder.get(j);
				Assert.assertTrue(comparator+".compare("+lesser+", "+t+")", comparator.compare(lesser, t) < 0);
			}
			Assert.assertEquals(comparator+".compare("+t+", "+t+")", 0, comparator.compare(t, t));
			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				T greater = valuesInExpectedOrder.get(j);
				Assert.assertTrue(comparator+".compare("+greater+", "+t+")", comparator.compare(greater, t) > 0);
			}
		}
	}

	public static <T extends Comparable<T>> void testCompareToAndEquals(ObjectList<T> valuesInExpectedOrder) {
		for (int i = 0; i < valuesInExpectedOrder.size(); i++) {
			T t = valuesInExpectedOrder.get(i);

			for (int j = 0; j < i; j++) {
				T lesser = valuesInExpectedOrder.get(j);
				Assert.assertTrue(lesser+".compareTo("+t+')', lesser.compareTo(t) < 0);
				Assert.assertFalse(lesser == t);
			}

			Assert.assertEquals(t+".compareTo("+t+')', 0, t.compareTo(t));
			Assert.assertTrue(equals(t, t));

			for (int j = i+1; j < valuesInExpectedOrder.size(); j++) {
				T greater = valuesInExpectedOrder.get(j);
				Assert.assertTrue(greater+".compareTo("+t+')', greater.compareTo(t) > 0);
				Assert.assertFalse(greater == t);
			}
		}
	}

	public static <T> ObjectCollection<T> misleadingSizeCollection(final int delta) {
		return new ObjectArrayList<T>() {
			@Override
			public int size() {
				return Math.max(0, super.size()+delta);
			}
		};
	}
}
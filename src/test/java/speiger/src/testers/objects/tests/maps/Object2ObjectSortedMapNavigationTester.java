package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.Helpers.assertEqualInOrder;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectSortedMap;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ObjectMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ObjectSortedMapNavigationTester<T, V> extends AbstractObject2ObjectMapTester<T, V>
{
	private Object2ObjectSortedMap<T, V> sortedMap;
	private Entry<T, V> a;
	private Entry<T, V> c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedMap = (Object2ObjectSortedMap<T, V>) getMap();
		ObjectList<Entry<T, V>> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		if (entries.size() >= 1) {
			a = entries.get(0);
			if (entries.size() >= 3) {
				c = entries.get(2);
			}
		}
	}

	public static <T, V> Comparator<Entry<T, V>> entryComparator(Comparator<T> keyComparator) {
		return new Comparator<Entry<T, V>>() {
			@Override
			public int compare(Entry<T, V> a, Entry<T, V> b) {
				return (keyComparator == null) ? ((Comparable<T>)a.getKey()).compareTo((T)b.getKey()) : keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptyMapFirst() {
		try {
			sortedMap.firstKey();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptyMapLast() {
		try {
			assertEquals(0L, sortedMap.lastKey());
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapFirst() {
		assertEquals(a.getKey(), sortedMap.firstKey());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonMapLast() {
		assertEquals(a.getKey(), sortedMap.lastKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a.getKey(), sortedMap.firstKey());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c.getKey(), sortedMap.lastKey());
	}

	@CollectionSize.Require(absent = ZERO)
	public void testHeadMapExclusive() {
		assertFalse(sortedMap.headMap(a.getKey()).containsKey(a.getKey()));
	}

	@CollectionSize.Require(absent = ZERO)
	public void testTailMapInclusive() {
		assertTrue(sortedMap.tailMap(a.getKey()).containsKey(a.getKey()));
	}
	
	@CollectionSize.Require(absent = SEVERAL)
	public void testSubMap() {
		ObjectList<Entry<T, V>> entries = ObjectHelpers.copyToList(getSampleElements(getNumElements()));
		entries.sort(entryComparator(sortedMap.comparator()));
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				assertEqualInOrder(entries.subList(i, j), sortedMap.subMap(entries.get(i).getKey(), entries.get(j).getKey()).object2ObjectEntrySet());
			}
		}
	}

	@CollectionSize.Require(SEVERAL)
	public void testSubMapIllegal() {
		try {
			sortedMap.subMap(c.getKey(), a.getKey());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
		}
	}

	@CollectionSize.Require(absent = ZERO)
	public void testOrderedByComparator() {
		@SuppressWarnings("unchecked")
		Comparator<T> comparator = sortedMap.comparator();
		if (comparator == null) {
			comparator = (Comparator<T>)Comparator.naturalOrder();
		}
		Iterator<Entry<T, V>> entryItr = sortedMap.object2ObjectEntrySet().iterator();
		Entry<T, V> prevEntry = entryItr.next();
		while (entryItr.hasNext()) {
			Entry<T, V> nextEntry = entryItr.next();
			assertTrue(comparator.compare(prevEntry.getKey(), nextEntry.getKey()) < 0);
			prevEntry = nextEntry;
		}
	}
}
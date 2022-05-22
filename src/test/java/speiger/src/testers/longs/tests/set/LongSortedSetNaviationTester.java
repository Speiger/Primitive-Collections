package speiger.src.testers.longs.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.testers.longs.tests.base.AbstractLongSetTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class LongSortedSetNaviationTester extends AbstractLongSetTester {
	private LongSortedSet sortedSet;
	private LongList values;
	private long a;
	private long c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedSet = (LongSortedSet) getSet();
		values = LongHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(sortedSet.comparator());

		// some tests assume SEVERAL == 3
		if (values.size() >= 1) {
			a = values.getLong(0);
			if (values.size() >= 3) {
				c = values.getLong(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(Long.MAX_VALUE, sortedSet.pollFirstLong());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(Long.MIN_VALUE, sortedSet.pollLastLong());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, sortedSet.pollFirstLong());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, sortedSet.pollLastLong());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, sortedSet.pollFirstLong());
		assertEquals(values.subList(1, values.size()), LongHelpers.copyToList(sortedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, sortedSet.pollLastLong());
		assertEquals(values.subList(0, values.size()-1), LongHelpers.copyToList(sortedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			sortedSet.pollFirstLong();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			sortedSet.pollLastLong();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			sortedSet.firstLong();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			sortedSet.lastLong();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, sortedSet.firstLong());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, sortedSet.lastLong());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, sortedSet.firstLong());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, sortedSet.lastLong());
	}
}
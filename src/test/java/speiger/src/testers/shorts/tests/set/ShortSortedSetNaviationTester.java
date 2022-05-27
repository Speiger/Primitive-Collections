package speiger.src.testers.shorts.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.testers.shorts.tests.base.AbstractShortSetTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ShortSortedSetNaviationTester extends AbstractShortSetTester
{
	private ShortSortedSet sortedSet;
	private ShortList values;
	private short a;
	private short c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedSet = (ShortSortedSet) getSet();
		values = ShortHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(sortedSet.comparator());

		// some tests assume SEVERAL == 3
		if (values.size() >= 1) {
			a = values.getShort(0);
			if (values.size() >= 3) {
				c = values.getShort(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(Short.MAX_VALUE, sortedSet.pollFirstShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(Short.MIN_VALUE, sortedSet.pollLastShort());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, sortedSet.pollFirstShort());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, sortedSet.pollLastShort());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, sortedSet.pollFirstShort());
		assertEquals(values.subList(1, values.size()), ShortHelpers.copyToList(sortedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, sortedSet.pollLastShort());
		assertEquals(values.subList(0, values.size()-1), ShortHelpers.copyToList(sortedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			sortedSet.pollFirstShort();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			sortedSet.pollLastShort();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			sortedSet.firstShort();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			sortedSet.lastShort();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, sortedSet.firstShort());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, sortedSet.lastShort());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, sortedSet.firstShort());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, sortedSet.lastShort());
	}
}
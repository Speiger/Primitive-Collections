package speiger.src.testers.bytes.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.NoSuchElementException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.testers.bytes.tests.base.AbstractByteSetTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ByteSortedSetNaviationTester extends AbstractByteSetTester
{
	private ByteSortedSet sortedSet;
	private ByteList values;
	private byte a;
	private byte c;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sortedSet = (ByteSortedSet) getSet();
		values = ByteHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
		values.sort(sortedSet.comparator());

		// some tests assume SEVERAL == 3
		if (values.size() >= 1) {
			a = values.getByte(0);
			if (values.size() >= 3) {
				c = values.getByte(2);
			}
		}
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollFirst() {
		assertEquals(Byte.MAX_VALUE, sortedSet.pollFirstByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		assertEquals(Byte.MIN_VALUE, sortedSet.pollLastByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, sortedSet.pollFirstByte());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, sortedSet.pollLastByte());
		assertTrue(sortedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, sortedSet.pollFirstByte());
		assertEquals(values.subList(1, values.size()), ByteHelpers.copyToList(sortedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, sortedSet.pollLastByte());
		assertEquals(values.subList(0, values.size()-1), ByteHelpers.copyToList(sortedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			sortedSet.pollFirstByte();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			sortedSet.pollLastByte();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			sortedSet.firstByte();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			sortedSet.lastByte();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, sortedSet.firstByte());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, sortedSet.lastByte());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, sortedSet.firstByte());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, sortedSet.lastByte());
	}
}
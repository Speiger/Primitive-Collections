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
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.testers.bytes.tests.base.AbstractByteSetTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ByteOrderedSetNavigationTester extends AbstractByteSetTester
{
	private ByteOrderedSet orderedSet;
	private ByteList values;
	private byte a;
	private byte c;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		orderedSet = (ByteOrderedSet) getSet();
		values = ByteHelpers.copyToList(getSampleElements(getSubjectGenerator().getCollectionSize().getNumElements()));
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
		try {
			orderedSet.pollFirstByte();
			fail("OrderedSet.pollFirstByte should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ZERO)
	public void testEmptySetPollLast() {
		try {
			orderedSet.pollLastByte();
			fail("OrderedSet.pollLastByte should throw NoSuchElementException");
		} catch (NoSuchElementException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollFirst() {
		try {
			orderedSet.pollFirstByte();
			fail("OrderedSet.pollFirstByte should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testUnsupportedSetPollLast() {
		try {
			orderedSet.pollLastByte();
			fail("OrderedSet.pollLastByte should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollFirst() {
		assertEquals(a, orderedSet.pollFirstByte());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(ONE)
	public void testSingletonSetPollLast() {
		assertEquals(a, orderedSet.pollLastByte());
		assertTrue(orderedSet.isEmpty());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollFirst() {
		assertEquals(a, orderedSet.pollFirstByte());
		assertEquals(values.subList(1, values.size()), ByteHelpers.copyToList(orderedSet));
	}

	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(SEVERAL)
	public void testPollLast() {
		assertEquals(c, orderedSet.pollLastByte());
		assertEquals(values.subList(0, values.size()-1), ByteHelpers.copyToList(orderedSet));
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollFirstUnsupported() {
		try {
			orderedSet.pollFirstByte();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	public void testPollLastUnsupported() {
		try {
			orderedSet.pollLastByte();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@CollectionSize.Require(ZERO)
	public void testEmptySetFirst() {
		try {
			orderedSet.firstByte();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ZERO)
	public void testEmptySetLast() {
		try {
			orderedSet.lastByte();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetFirst() {
		assertEquals(a, orderedSet.firstByte());
	}

	@CollectionSize.Require(ONE)
	public void testSingletonSetLast() {
		assertEquals(a, orderedSet.lastByte());
	}

	@CollectionSize.Require(SEVERAL)
	public void testFirst() {
		assertEquals(a, orderedSet.firstByte());
	}

	@CollectionSize.Require(SEVERAL)
	public void testLast() {
		assertEquals(c, orderedSet.lastByte());
	}
}
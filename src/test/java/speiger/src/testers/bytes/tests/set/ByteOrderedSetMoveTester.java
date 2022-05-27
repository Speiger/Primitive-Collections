package speiger.src.testers.bytes.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static org.junit.Assert.assertNotEquals;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.testers.bytes.tests.base.AbstractByteSetTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ByteOrderedSetMoveTester extends AbstractByteSetTester
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
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstByte());
		assertEquals(true, orderedSet.addAndMoveToFirst(e4()));
		assertNotEquals(a, orderedSet.firstByte());
		assertEquals(e4(), orderedSet.firstByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstByte());
		assertEquals(false, orderedSet.addAndMoveToFirst(c));
		assertNotEquals(a, orderedSet.firstByte());
		assertEquals(c, orderedSet.firstByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastByte());
		assertEquals(true, orderedSet.addAndMoveToLast(e4()));
		assertNotEquals(c, orderedSet.lastByte());
		assertEquals(e4(), orderedSet.lastByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testaddMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastByte());
		assertEquals(false, orderedSet.addAndMoveToLast(a));
		assertNotEquals(c, orderedSet.lastByte());
		assertEquals(a, orderedSet.lastByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstMissing()
	{
		assertEquals(a, orderedSet.firstByte());
		assertEquals(false, orderedSet.moveToFirst(e4()));
		assertEquals(a, orderedSet.firstByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToFirstPreset()
	{
		assertEquals(a, orderedSet.firstByte());
		assertEquals(true, orderedSet.moveToFirst(c));
		assertNotEquals(a, orderedSet.firstByte());
		assertEquals(c, orderedSet.firstByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastMissing()
	{
		assertEquals(c, orderedSet.lastByte());
		assertEquals(false, orderedSet.moveToLast(e4()));
		assertEquals(c, orderedSet.lastByte());
	}
	
	@CollectionFeature.Require(SUPPORTS_ADD)
	@CollectionSize.Require(SEVERAL)
	public void testMoveToLastPreset()
	{
		assertEquals(c, orderedSet.lastByte());
		assertEquals(true, orderedSet.moveToLast(a));
		assertNotEquals(c, orderedSet.lastByte());
		assertEquals(a, orderedSet.lastByte());
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testaddMoveToFirstUnsupported()
	{
		try {
			orderedSet.addAndMoveToFirst(e4());
			fail("addAndMoveToFirst should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testaddMoveToLastUnsupported()
	{
		try {
			orderedSet.addAndMoveToLast(e4());
			fail("addAndMoveToLast should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testmoveToFirstUnsupported()
	{
		try {
			orderedSet.moveToFirst(c);
			fail("moveToFirst should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(absent = SUPPORTS_ADD)
	public void testmoveToLastUnsupported()
	{
		try {
			orderedSet.moveToLast(a);
			fail("moveToLast should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
		expectUnchanged();
	}
}
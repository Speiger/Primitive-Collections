package speiger.src.testers.bytes.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;
import speiger.src.testers.bytes.utils.ByteHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ByteCollectionForEachTester extends AbstractByteCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		ByteList elements = new ByteArrayList();
		collection.forEach(elements::add);
		ByteHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		ByteList elements = new ByteArrayList();
		collection.forEach(elements, ByteList::add);
		ByteHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ByteList elements = new ByteArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", ByteHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		ByteList elements = new ByteArrayList();
		collection.forEach(elements, ByteList::add);
		assertEquals("Different ordered iteration", ByteHelpers.copyToList(getOrderedElements()), elements);
	}
}
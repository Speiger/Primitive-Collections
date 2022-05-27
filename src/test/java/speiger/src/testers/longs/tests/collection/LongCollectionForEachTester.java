package speiger.src.testers.longs.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
public class LongCollectionForEachTester extends AbstractLongCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		LongList elements = new LongArrayList();
		collection.forEach(elements::add);
		LongHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		LongList elements = new LongArrayList();
		collection.forEach(elements, LongList::add);
		LongHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		LongList elements = new LongArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", LongHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		LongList elements = new LongArrayList();
		collection.forEach(elements, LongList::add);
		assertEquals("Different ordered iteration", LongHelpers.copyToList(getOrderedElements()), elements);
	}
}
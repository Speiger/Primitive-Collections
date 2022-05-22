package speiger.src.testers.shorts.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

public class ShortCollectionForEachTester extends AbstractShortCollectionTester {
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		ShortList elements = new ShortArrayList();
		collection.forEach(elements::add);
		ShortHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		ShortList elements = new ShortArrayList();
		collection.forEach(elements, ShortList::add);
		ShortHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ShortList elements = new ShortArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", ShortHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		ShortList elements = new ShortArrayList();
		collection.forEach(elements, ShortList::add);
		assertEquals("Different ordered iteration", ShortHelpers.copyToList(getOrderedElements()), elements);
	}
}
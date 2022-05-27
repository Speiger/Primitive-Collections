package speiger.src.testers.objects.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectCollectionForEachTester<T> extends AbstractObjectCollectionTester<T>
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		ObjectList<T> elements = new ObjectArrayList<>();
		collection.forEach(elements::add);
		ObjectHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		ObjectList<T> elements = new ObjectArrayList<>();
		collection.forEach(elements, ObjectList::add);
		ObjectHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ObjectList<T> elements = new ObjectArrayList<>();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", ObjectHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		ObjectList<T> elements = new ObjectArrayList<>();
		collection.forEach(elements, ObjectList::add);
		assertEquals("Different ordered iteration", ObjectHelpers.copyToList(getOrderedElements()), elements);
	}
}
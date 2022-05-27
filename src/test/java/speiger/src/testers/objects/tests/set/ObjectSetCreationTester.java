package speiger.src.testers.objects.tests.set;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.REJECTS_DUPLICATES_AT_CREATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.AbstractObjectSetTester;

@Ignore
public class ObjectSetCreationTester<T> extends AbstractObjectSetTester<T>
{
	@CollectionFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		T[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		expectContents(ObjectArrayList.wrap(array).subList(1, getNumElements()));
	}
	
	@CollectionFeature.Require(REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesRejected() {
		T[] array = createSamplesArray();
		array[1] = e0();
		try {
			collection = primitiveGenerator.create(array);
			fail("Should reject duplicate non-null elements at creation");
		} catch (IllegalArgumentException expected) {
		}
	}
}
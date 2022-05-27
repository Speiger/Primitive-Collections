package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2BooleanMapCreatorTester<T> extends AbstractObject2BooleanMapTester<T>
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Object2BooleanMap.Entry<T>[] getEntriesMultipleNonNullKeys() {
		Object2BooleanMap.Entry<T>[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Object2BooleanMap.Entry<T>[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}
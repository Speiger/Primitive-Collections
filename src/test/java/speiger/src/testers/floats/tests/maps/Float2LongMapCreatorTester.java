package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2LongMapCreatorTester extends AbstractFloat2LongMapTester
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Float2LongMap.Entry[] getEntriesMultipleNonNullKeys() {
		Float2LongMap.Entry[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Float2LongMap.Entry[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}
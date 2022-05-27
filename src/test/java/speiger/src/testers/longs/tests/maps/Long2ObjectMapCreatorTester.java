package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ObjectMapCreatorTester<V> extends AbstractLong2ObjectMapTester<V>
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Long2ObjectMap.Entry<V>[] getEntriesMultipleNonNullKeys() {
		Long2ObjectMap.Entry<V>[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Long2ObjectMap.Entry<V>[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}
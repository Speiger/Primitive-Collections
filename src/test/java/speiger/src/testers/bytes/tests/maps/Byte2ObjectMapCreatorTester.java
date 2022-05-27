package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ObjectMapCreatorTester<V> extends AbstractByte2ObjectMapTester<V>
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Byte2ObjectMap.Entry<V>[] getEntriesMultipleNonNullKeys() {
		Byte2ObjectMap.Entry<V>[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Byte2ObjectMap.Entry<V>[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}
package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.bytes.maps.interfaces.Byte2ByteMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ByteMapCreatorTester extends AbstractByte2ByteMapTester
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Byte2ByteMap.Entry[] getEntriesMultipleNonNullKeys() {
		Byte2ByteMap.Entry[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Byte2ByteMap.Entry[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}
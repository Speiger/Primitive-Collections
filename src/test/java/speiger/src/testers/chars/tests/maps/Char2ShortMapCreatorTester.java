package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2ShortMapCreatorTester extends AbstractChar2ShortMapTester
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Char2ShortMap.Entry[] getEntriesMultipleNonNullKeys() {
		Char2ShortMap.Entry[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Char2ShortMap.Entry[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}
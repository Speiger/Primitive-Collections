package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.utils.CharSamples;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2CharMapTester;
import speiger.src.testers.bytes.utils.ByteSamples;

@Ignore
public class Byte2CharMapReplaceAllTester extends AbstractByte2CharMapTester
{
	private ByteSamples keys() {
		return new ByteSamples(k0(), k1(), k2(), k3(), k4());
	}

	private CharSamples values() {
		return new CharSamples(v0(), v1(), v2(), v3(), v4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceAllRotate() {
		getMap().replaceChars((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getChar(index + 1);
		});
		ObjectList<Byte2CharMap.Entry> expectedEntries = new ObjectArrayList<>();
		for (Byte2CharMap.Entry entry : getSampleEntries()) {
			int index = keys().asList().indexOf(entry.getByteKey());
			expectedEntries.add(entry(entry.getByteKey(), values().asList().getChar(index + 1)));
		}
		expectContents(expectedEntries);
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testReplaceAllPreservesOrder() {
		getMap().replaceChars((k, v) -> {
			int index = keys().asList().indexOf(k);
			return values().asList().getChar(index + 1);
		});
		ObjectList<Byte2CharMap.Entry> orderedEntries = getOrderedElements();
		int index = 0;
		for (byte key : getMap().keySet()) {
			assertEquals(orderedEntries.get(index).getByteKey(), key);
			index++;
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceAll_unsupported() {
		try {
			getMap().replaceChars((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getChar(index + 1);
			});
			fail("replaceChars() should throw UnsupportedOperation if a map does " + "not support it and is not empty.");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(ZERO)
	public void testReplaceAll_unsupportedByEmptyCollection() {
		try {
			getMap().replaceChars((k, v) -> {
				int index = keys().asList().indexOf(k);
				return values().asList().getChar(index + 1);
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceAll_unsupportedNoOpFunction() {
		try {
			getMap().replaceChars((k, v) -> v);
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
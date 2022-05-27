package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2ByteMapRemoveEntryTester extends AbstractChar2ByteMapTester
{
	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_supportedPresent() {
		assertTrue(getMap().remove(k0(), v0()));
		expectMissing(e0());
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testRemove_supportedPresentKeyWrongValue() {
		assertFalse(getMap().remove(k0(), v3()));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testRemove_supportedWrongKeyPresentValue() {
		assertFalse(getMap().remove(k3(), v0()));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	public void testRemove_supportedAbsentKeyAbsentValue() {
		assertFalse(getMap().remove(k3(), v3()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testRemove_unsupportedPresent() {
		try {
			getMap().remove(k0(), v0());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_REMOVE)
	public void testRemove_unsupportedAbsent() {
		try {
			assertFalse(getMap().remove(k0(), v3()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
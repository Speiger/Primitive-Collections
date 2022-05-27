package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2FloatMapRemoveEntryTester extends AbstractLong2FloatMapTester
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
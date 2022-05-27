package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ObjectMapTester;

@Ignore
public class Double2ObjectMapPutIfAbsentTester<V> extends AbstractDouble2ObjectMapTester<V>
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutIfAbsent_supportedAbsent() {
		assertEquals("putIfAbsent(notPresent, value) should return null", null, getMap().putIfAbsent(k3(), v3()));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutIfAbsent_supportedPresent() {
		assertEquals("putIfAbsent(present, value) should return existing value", v0(), getMap().putIfAbsent(k0(), v3()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutIfAbsent_unsupportedAbsent() {
		try {
			getMap().putIfAbsent(k3(), v3());
			fail("putIfAbsent(notPresent, value) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("putIfAbsent(present, existingValue) should return present or throw", v0(), getMap().putIfAbsent(k0(), v0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutIfAbsent_unsupportedPresentDifferentValue() {
		try {
			getMap().putIfAbsent(k0(), v3());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2BooleanMapReplaceTester extends AbstractByte2BooleanMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplace_supportedPresent() {
		try {
			assertEquals(v0(), getMap().replace(k0(), v3()));
			expectReplacement(entry(k0(), v3()));
		} catch (ClassCastException tolerated) { // for ClassToInstanceMap
			expectUnchanged();
		}
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplace_supportedPresentNoChange() {
		assertEquals(v0(), getMap().replace(k0(), v0()));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplace_supportedAbsent() {
		assertEquals(false, getMap().replace(k3(), v3()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplace_unsupportedPresent() {
		try {
			getMap().replace(k0(), v3());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		} catch (ClassCastException tolerated) {
			// for ClassToInstanceMap
		}
		expectUnchanged();
	}
}
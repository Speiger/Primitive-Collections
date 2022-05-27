package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ByteMapTester;

@Ignore
public class Byte2ByteMapComputeIfPresentTester extends AbstractByte2ByteMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfPresent_supportedAbsent() {
		assertEquals("computeByteIfPresent(notPresent, function) should return (byte)-1", (byte)-1,
				getMap().computeByteIfPresent(k3(), (k, v) -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_supportedPresent() {
		assertEquals("computeByteIfPresent(present, function) should return new value", v3(),
				getMap().computeByteIfPresent(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return v3();
				}));
		expectReplacement(entry(k0(), v3()));
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_functionReturnsNull() {
		assertEquals("computeByteIfPresent(present, returnsNull) should return (byte)-1", (byte)-1,
				getMap().computeByteIfPresent(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return (byte)-1;
				}));
		expectMissing(e0());
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_functionThrows() {
		try {
			getMap().computeByteIfPresent(k0(), (k, v) -> {
				assertEquals(k0(), k);
				assertEquals(v0(), v);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testComputeIfPresent_unsupportedAbsent() {
		try {
			getMap().computeByteIfPresent(k3(), (k, v) -> {
				throw new AssertionFailedError();
			});
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_unsupportedPresent() {
		try {
			getMap().computeByteIfPresent(k0(), (k, v) -> v3());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}
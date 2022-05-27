package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2ByteMapTester;

@Ignore
public class Int2ByteMapComputeIfAbsentTester extends AbstractInt2ByteMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_supportedAbsent() {
		assertEquals("computeByteIfAbsent(notPresent, function) should return new value", v3(),
				getMap().computeByteIfAbsent(k3(), k -> {
					assertEquals(k3(), k);
					return v3();
				}));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_supportedPresent() {
		assertEquals("computeByteIfAbsent(present, function) should return existing value", v0(),
				getMap().computeByteIfAbsent(k0(), k -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("computeByteIfAbsent(absent, returnsNull) should return (byte)-1",
				(byte)-1, getMap().computeByteIfAbsent(k3(), k -> {
					assertEquals(k3(), k);
					return (byte)-1;
				}));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_functionThrows() {
		try {
			getMap().computeByteIfAbsent(k3(), k -> {
				assertEquals(k3(), k);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testComputeIfAbsent_unsupportedAbsent() {
		try {
			getMap().computeByteIfAbsent(k3(), k -> {
				// allowed to be called
				assertEquals(k3(), k);
				return v3();
			});
			fail("computeByteIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("computeByteIfAbsent(present, returnsCurrentValue) should return present or throw", v0(),
					getMap().computeByteIfAbsent(k0(), k -> {
						assertEquals(k0(), k);
						return v0();
					}));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_unsupportedPresentDifferentValue() {
		try {
			assertEquals("computeByteIfAbsent(present, returnsDifferentValue) should return present or throw", v0(),
					getMap().computeByteIfAbsent(k0(), k -> {
						assertEquals(k0(), k);
						return v3();
					}));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
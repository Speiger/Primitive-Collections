package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ShortMapComputeIfPresentTester extends AbstractDouble2ShortMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfPresent_supportedAbsent() {
		assertEquals("computeShortIfPresent(notPresent, function) should return (short)-1", (short)-1,
				getMap().computeShortIfPresent(k3(), (k, v) -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_supportedPresent() {
		assertEquals("computeShortIfPresent(present, function) should return new value", v3(),
				getMap().computeShortIfPresent(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return v3();
				}));
		expectReplacement(entry(k0(), v3()));
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_functionReturnsNull() {
		assertEquals("computeShortIfPresent(present, returnsNull) should return (short)-1", (short)-1,
				getMap().computeShortIfPresent(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return (short)-1;
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
			getMap().computeShortIfPresent(k0(), (k, v) -> {
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
			getMap().computeShortIfPresent(k3(), (k, v) -> {
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
			getMap().computeShortIfPresent(k0(), (k, v) -> v3());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}
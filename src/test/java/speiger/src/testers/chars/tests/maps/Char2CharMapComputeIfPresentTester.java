package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2CharMapTester;

@Ignore
public class Char2CharMapComputeIfPresentTester extends AbstractChar2CharMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfPresent_supportedAbsent() {
		assertEquals("computeCharIfPresent(notPresent, function) should return (char)-1", (char)-1,
				getMap().computeCharIfPresent(k3(), (k, v) -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_supportedPresent() {
		assertEquals("computeCharIfPresent(present, function) should return new value", v3(),
				getMap().computeCharIfPresent(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return v3();
				}));
		expectReplacement(entry(k0(), v3()));
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfPresent_functionReturnsNull() {
		assertEquals("computeCharIfPresent(present, returnsNull) should return (char)-1", (char)-1,
				getMap().computeCharIfPresent(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return (char)-1;
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
			getMap().computeCharIfPresent(k0(), (k, v) -> {
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
			getMap().computeCharIfPresent(k3(), (k, v) -> {
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
			getMap().computeCharIfPresent(k0(), (k, v) -> v3());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}
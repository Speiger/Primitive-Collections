package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2FloatMapComputeIfAbsentTester<T> extends AbstractObject2FloatMapTester<T>
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_supportedAbsent() {
		assertEquals("computeFloatIfAbsent(notPresent, function) should return new value", v3(),
				getMap().computeFloatIfAbsent(k3(), k -> {
					assertEquals(k3(), k);
					return v3();
				}));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_supportedPresent() {
		assertEquals("computeFloatIfAbsent(present, function) should return existing value", v0(),
				getMap().computeFloatIfAbsent(k0(), k -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("computeFloatIfAbsent(absent, returnsNull) should return -1F",
				-1F, getMap().computeFloatIfAbsent(k3(), k -> {
					assertEquals(k3(), k);
					return -1F;
				}));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_functionThrows() {
		try {
			getMap().computeFloatIfAbsent(k3(), k -> {
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
			getMap().computeFloatIfAbsent(k3(), k -> {
				// allowed to be called
				assertEquals(k3(), k);
				return v3();
			});
			fail("computeFloatIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("computeFloatIfAbsent(present, returnsCurrentValue) should return present or throw", v0(),
					getMap().computeFloatIfAbsent(k0(), k -> {
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
			assertEquals("computeFloatIfAbsent(present, returnsDifferentValue) should return present or throw", v0(),
					getMap().computeFloatIfAbsent(k0(), k -> {
						assertEquals(k0(), k);
						return v3();
					}));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
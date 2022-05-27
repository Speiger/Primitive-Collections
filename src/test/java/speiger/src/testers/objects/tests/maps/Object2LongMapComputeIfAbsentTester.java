package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2LongMapComputeIfAbsentTester<T> extends AbstractObject2LongMapTester<T>
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_supportedAbsent() {
		assertEquals("computeLongIfAbsent(notPresent, function) should return new value", v3(),
				getMap().computeLongIfAbsent(k3(), k -> {
					assertEquals(k3(), k);
					return v3();
				}));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_supportedPresent() {
		assertEquals("computeLongIfAbsent(present, function) should return existing value", v0(),
				getMap().computeLongIfAbsent(k0(), k -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("computeLongIfAbsent(absent, returnsNull) should return -1L",
				-1L, getMap().computeLongIfAbsent(k3(), k -> {
					assertEquals(k3(), k);
					return -1L;
				}));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testComputeIfAbsent_functionThrows() {
		try {
			getMap().computeLongIfAbsent(k3(), k -> {
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
			getMap().computeLongIfAbsent(k3(), k -> {
				// allowed to be called
				assertEquals(k3(), k);
				return v3();
			});
			fail("computeLongIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testComputeIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("computeLongIfAbsent(present, returnsCurrentValue) should return present or throw", v0(),
					getMap().computeLongIfAbsent(k0(), k -> {
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
			assertEquals("computeLongIfAbsent(present, returnsDifferentValue) should return present or throw", v0(),
					getMap().computeLongIfAbsent(k0(), k -> {
						assertEquals(k0(), k);
						return v3();
					}));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2FloatMapSupplyIfAbsentTester extends AbstractDouble2FloatMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_supportedAbsent() {
		assertEquals("supplyFloatIfAbsent(notPresent, function) should return new value", v3(),
				getMap().supplyFloatIfAbsent(k3(), this::v3));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_supportedPresent() {
		assertEquals("supplyFloatIfAbsent(present, function) should return existing value", v0(),
				getMap().supplyFloatIfAbsent(k0(), () -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("supplyFloatIfAbsent(absent, returnsNull) should return -1F", -1F, getMap().supplyFloatIfAbsent(k3(), () -> -1F));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionThrows() {
		try {
			getMap().supplyFloatIfAbsent(k3(), () -> {
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testSupplyIfAbsent_unsupportedAbsent() {
		try {
			getMap().supplyFloatIfAbsent(k3(), this::v3);
			fail("supplyFloatIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("supplyFloatIfAbsent(present, returnsCurrentValue) should return present or throw", v0(), getMap().supplyFloatIfAbsent(k0(), this::v0));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentDifferentValue() {
		try {
			assertEquals("supplyFloatIfAbsent(present, returnsDifferentValue) should return present or throw", v0(), getMap().supplyFloatIfAbsent(k0(), this::v3));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
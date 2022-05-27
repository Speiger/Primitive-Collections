package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2BooleanMapSupplyIfAbsentTester extends AbstractChar2BooleanMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_supportedAbsent() {
		assertEquals("supplyBooleanIfAbsent(notPresent, function) should return new value", v3(),
				getMap().supplyBooleanIfAbsent(k3(), this::v3));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_supportedPresent() {
		assertEquals("supplyBooleanIfAbsent(present, function) should return existing value", v0(),
				getMap().supplyBooleanIfAbsent(k0(), () -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("supplyBooleanIfAbsent(absent, returnsNull) should return false", false, getMap().supplyBooleanIfAbsent(k3(), () -> false));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionThrows() {
		try {
			getMap().supplyBooleanIfAbsent(k3(), () -> {
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
			getMap().supplyBooleanIfAbsent(k3(), this::v3);
			fail("supplyBooleanIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("supplyBooleanIfAbsent(present, returnsCurrentValue) should return present or throw", v0(), getMap().supplyBooleanIfAbsent(k0(), this::v0));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentDifferentValue() {
		try {
			assertEquals("supplyBooleanIfAbsent(present, returnsDifferentValue) should return present or throw", v0(), getMap().supplyBooleanIfAbsent(k0(), this::v3));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
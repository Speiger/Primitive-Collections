package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2IntMapSupplyIfAbsentTester extends AbstractDouble2IntMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_supportedAbsent() {
		assertEquals("supplyIntIfAbsent(notPresent, function) should return new value", v3(),
				getMap().supplyIntIfAbsent(k3(), this::v3));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_supportedPresent() {
		assertEquals("supplyIntIfAbsent(present, function) should return existing value", v0(),
				getMap().supplyIntIfAbsent(k0(), () -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("supplyIntIfAbsent(absent, returnsNull) should return -1", -1, getMap().supplyIntIfAbsent(k3(), () -> -1));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionThrows() {
		try {
			getMap().supplyIntIfAbsent(k3(), () -> {
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
			getMap().supplyIntIfAbsent(k3(), this::v3);
			fail("supplyIntIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("supplyIntIfAbsent(present, returnsCurrentValue) should return present or throw", v0(), getMap().supplyIntIfAbsent(k0(), this::v0));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentDifferentValue() {
		try {
			assertEquals("supplyIntIfAbsent(present, returnsDifferentValue) should return present or throw", v0(), getMap().supplyIntIfAbsent(k0(), this::v3));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
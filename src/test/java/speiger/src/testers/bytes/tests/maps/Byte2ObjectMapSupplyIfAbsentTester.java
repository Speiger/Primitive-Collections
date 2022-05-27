package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2ObjectMapSupplyIfAbsentTester<V> extends AbstractByte2ObjectMapTester<V>
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_supportedAbsent() {
		assertEquals("supplyIfAbsent(notPresent, function) should return new value", v3(),
				getMap().supplyIfAbsent(k3(), this::v3));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_supportedPresent() {
		assertEquals("supplyIfAbsent(present, function) should return existing value", v0(),
				getMap().supplyIfAbsent(k0(), () -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("supplyIfAbsent(absent, returnsNull) should return null", null, getMap().supplyIfAbsent(k3(), () -> null));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionThrows() {
		try {
			getMap().supplyIfAbsent(k3(), () -> {
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
			getMap().supplyIfAbsent(k3(), this::v3);
			fail("supplyIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("supplyIfAbsent(present, returnsCurrentValue) should return present or throw", v0(), getMap().supplyIfAbsent(k0(), this::v0));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentDifferentValue() {
		try {
			assertEquals("supplyIfAbsent(present, returnsDifferentValue) should return present or throw", v0(), getMap().supplyIfAbsent(k0(), this::v3));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
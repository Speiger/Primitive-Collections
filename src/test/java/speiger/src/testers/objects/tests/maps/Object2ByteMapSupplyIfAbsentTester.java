package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ByteMapSupplyIfAbsentTester<T> extends AbstractObject2ByteMapTester<T>
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_supportedAbsent() {
		assertEquals("supplyByteIfAbsent(notPresent, function) should return new value", v3(),
				getMap().supplyByteIfAbsent(k3(), this::v3));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_supportedPresent() {
		assertEquals("supplyByteIfAbsent(present, function) should return existing value", v0(),
				getMap().supplyByteIfAbsent(k0(), () -> {
					throw new AssertionFailedError();
				}));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionReturnsNullNotInserted() {
		assertEquals("supplyByteIfAbsent(absent, returnsNull) should return (byte)-1", (byte)-1, getMap().supplyByteIfAbsent(k3(), () -> (byte)-1));
		expectUnchanged();
	}

	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testSupplyIfAbsent_functionThrows() {
		try {
			getMap().supplyByteIfAbsent(k3(), () -> {
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
			getMap().supplyByteIfAbsent(k3(), this::v3);
			fail("supplyByteIfAbsent(notPresent, function) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentExistingValue() {
		try {
			assertEquals("supplyByteIfAbsent(present, returnsCurrentValue) should return present or throw", v0(), getMap().supplyByteIfAbsent(k0(), this::v0));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSupplyIfAbsent_unsupportedPresentDifferentValue() {
		try {
			assertEquals("supplyByteIfAbsent(present, returnsDifferentValue) should return present or throw", v0(), getMap().supplyByteIfAbsent(k0(), this::v3));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}
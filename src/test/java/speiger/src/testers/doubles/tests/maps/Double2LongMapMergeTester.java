package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import junit.framework.AssertionFailedError;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2LongMapTester;

@Ignore
public class Double2LongMapMergeTester extends AbstractDouble2LongMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testAbsent() {
		assertEquals("Map.mergeLong(absent, value, function) should return value", v3(),
				getMap().mergeLong(k3(), v3(), (oldV, newV) -> {
					throw new AssertionFailedError("Should not call mergeLong function if key was absent");
				}));
		expectAdded(e3());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testMergePresent() {
		assertEquals("Map.mergeLong(present, value, function) should return function result", v4(),
				getMap().mergeLong(k0(), v3(), (oldV, newV) -> {
					assertEquals(v0(), oldV);
					assertEquals(v3(), newV);
					return v4();
				}));
		expectReplacement(entry(k0(), v4()));
	}

	private static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testMergeFunctionThrows() {
		try {
			getMap().mergeLong(k0(), v3(), (oldV, newV) -> {
				assertEquals(v0(), oldV);
				assertEquals(v3(), newV);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = ZERO)
	public void testMergePresentToNull() {
		assertEquals("Map.mergeLong(present, value, functionReturningNull) should return -1L", -1L,
				getMap().mergeLong(k0(), v3(), (oldV, newV) -> {
					assertEquals(v0(), oldV);
					assertEquals(v3(), newV);
					return -1L;
				}));
		expectMissing(e0());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testMergeUnsupported() {
		try {
			getMap().mergeLong(k3(), v3(), (oldV, newV) -> {
				throw new AssertionFailedError();
			});
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
	}
}
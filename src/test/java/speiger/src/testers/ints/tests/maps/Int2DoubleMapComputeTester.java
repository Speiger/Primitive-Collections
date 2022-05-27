package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2DoubleMapTester;

@Ignore
public class Int2DoubleMapComputeTester extends AbstractInt2DoubleMapTester
{
	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentToPresent() {
		assertEquals("Map.computeDouble(absent, functionReturningValue) should return value", v3(),
				getMap().computeDouble(k3(), (k, v) -> {
					assertEquals(k3(), k);
					assertEquals(-1D, v);
					return v3();
				}));
		expectAdded(e3());
		assertEquals(getNumElements() + 1, getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentToAbsent() {
		assertEquals("Map.computeDouble(absent, functionReturningNull) should return -1D", -1D, getMap().computeDouble(k3(), (k, v) -> {
			assertEquals(k3(), k);
			assertEquals(-1D, v);
			return -1D;
		}));
		expectUnchanged();
		assertEquals(getNumElements(), getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	@CollectionSize.Require(absent = ZERO)
	public void testCompute_presentToPresent() {
		assertEquals("Map.computeDouble(present, functionReturningValue) should return new value", v3(),
				getMap().computeDouble(k0(), (k, v) -> {
					assertEquals(k0(), k);
					assertEquals(v0(), v);
					return v3();
				}));
		expectReplacement(entry(k0(), v3()));
		assertEquals(getNumElements(), getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	@CollectionSize.Require(absent = ZERO)
	public void testCompute_presentToAbsent() {
		assertEquals("Map.computeDouble(present, functionReturningNull) should return -1D", -1D, getMap().computeDouble(k0(), (k, v) -> {
			assertEquals(k0(), k);
			assertEquals(v0(), v);
			return -1D;
		}));
		expectMissing(e0());
		expectMissingKeys(k0());
		assertEquals(getNumElements() - 1, getMap().size());
	}
	
	static class ExpectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	@CollectionSize.Require(absent = ZERO)
	public void testCompute_presentFunctionThrows() {
		try {
			getMap().computeDouble(k0(), (k, v) -> {
				assertEquals(k0(), k);
				assertEquals(v0(), v);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentFunctionThrows() {
		try {
			getMap().computeDouble(k3(), (k, v) -> {
				assertEquals(k3(), k);
				assertEquals(-1D, v);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}
}
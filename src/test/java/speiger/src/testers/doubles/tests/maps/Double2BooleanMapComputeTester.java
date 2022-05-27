package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2BooleanMapComputeTester extends AbstractDouble2BooleanMapTester
{
	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentToPresent() {
		assertEquals("Map.computeBoolean(absent, functionReturningValue) should return value", v3(),
				getMap().computeBoolean(k3(), (k, v) -> {
					assertEquals(k3(), k);
					assertEquals(false, v);
					return v3();
				}));
		expectAdded(e3());
		assertEquals(getNumElements() + 1, getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentToAbsent() {
		assertEquals("Map.computeBoolean(absent, functionReturningNull) should return false", false, getMap().computeBoolean(k3(), (k, v) -> {
			assertEquals(k3(), k);
			assertEquals(false, v);
			return false;
		}));
		expectUnchanged();
		assertEquals(getNumElements(), getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	@CollectionSize.Require(absent = ZERO)
	public void testCompute_presentToPresent() {
		assertEquals("Map.computeBoolean(present, functionReturningValue) should return new value", v3(),
				getMap().computeBoolean(k0(), (k, v) -> {
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
		assertEquals("Map.computeBoolean(present, functionReturningNull) should return false", false, getMap().computeBoolean(k0(), (k, v) -> {
			assertEquals(k0(), k);
			assertEquals(v0(), v);
			return false;
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
			getMap().computeBoolean(k0(), (k, v) -> {
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
			getMap().computeBoolean(k3(), (k, v) -> {
				assertEquals(k3(), k);
				assertEquals(false, v);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}
}
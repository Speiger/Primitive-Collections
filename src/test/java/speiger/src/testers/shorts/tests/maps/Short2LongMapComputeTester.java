package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2LongMapComputeTester extends AbstractShort2LongMapTester
{
	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentToPresent() {
		assertEquals("Map.computeLong(absent, functionReturningValue) should return value", v3(),
				getMap().computeLong(k3(), (k, v) -> {
					assertEquals(k3(), k);
					assertEquals(-1L, v);
					return v3();
				}));
		expectAdded(e3());
		assertEquals(getNumElements() + 1, getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	public void testCompute_absentToAbsent() {
		assertEquals("Map.computeLong(absent, functionReturningNull) should return -1L", -1L, getMap().computeLong(k3(), (k, v) -> {
			assertEquals(k3(), k);
			assertEquals(-1L, v);
			return -1L;
		}));
		expectUnchanged();
		assertEquals(getNumElements(), getMap().size());
	}

	@MapFeature.Require({ SUPPORTS_PUT, SUPPORTS_REMOVE })
	@CollectionSize.Require(absent = ZERO)
	public void testCompute_presentToPresent() {
		assertEquals("Map.computeLong(present, functionReturningValue) should return new value", v3(),
				getMap().computeLong(k0(), (k, v) -> {
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
		assertEquals("Map.computeLong(present, functionReturningNull) should return -1L", -1L, getMap().computeLong(k0(), (k, v) -> {
			assertEquals(k0(), k);
			assertEquals(v0(), v);
			return -1L;
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
			getMap().computeLong(k0(), (k, v) -> {
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
			getMap().computeLong(k3(), (k, v) -> {
				assertEquals(k3(), k);
				assertEquals(-1L, v);
				throw new ExpectedException();
			});
			fail("Expected ExpectedException");
		} catch (ExpectedException expected) {
		}
		expectUnchanged();
	}
}
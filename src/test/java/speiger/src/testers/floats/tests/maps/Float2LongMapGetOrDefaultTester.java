package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2LongMapGetOrDefaultTester extends AbstractFloat2LongMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testGetOrDefault_present() {
		assertEquals("getOrDefault(present, def) should return the associated value", v0(), getMap().getOrDefault(k0(), v3()));
	}
	
	public void testGetOrDefault_absent() {
		assertEquals("getOrDefault(absent, def) should return the default value", v3(), getMap().getOrDefault(k3(), v3()));
	}
}
package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ShortMapTester;

@Ignore
public class Double2ShortMapGetOrDefaultTester extends AbstractDouble2ShortMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testGetOrDefault_present() {
		assertEquals("getOrDefault(present, def) should return the associated value", v0(), getMap().getOrDefault(k0(), v3()));
	}
	
	public void testGetOrDefault_absent() {
		assertEquals("getOrDefault(absent, def) should return the default value", v3(), getMap().getOrDefault(k3(), v3()));
	}
}
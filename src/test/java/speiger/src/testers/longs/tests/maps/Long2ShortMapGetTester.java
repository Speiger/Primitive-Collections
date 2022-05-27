package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2ShortMapTester;

@Ignore
public class Long2ShortMapGetTester extends AbstractLong2ShortMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testGet_yes() {
		assertEquals("get(present) should return the associated value", v0(), get(k0()));
	}
	
	public void testGet_no() {
		assertEquals("get(notPresent) should return (short)-1", (short)-1, get(k3()));
	}
}
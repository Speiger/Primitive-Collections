package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2DoubleMapGetTester extends AbstractDouble2DoubleMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testGet_yes() {
		assertEquals("get(present) should return the associated value", v0(), get(k0()));
	}
	
	public void testGet_no() {
		assertEquals("get(notPresent) should return -1D", -1D, get(k3()));
	}
}
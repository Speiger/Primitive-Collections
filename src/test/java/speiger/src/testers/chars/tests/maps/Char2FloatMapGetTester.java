package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2FloatMapGetTester extends AbstractChar2FloatMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testGet_yes() {
		assertEquals("get(present) should return the associated value", v0(), get(k0()));
	}
	
	public void testGet_no() {
		assertEquals("get(notPresent) should return -1F", -1F, get(k3()));
	}
}
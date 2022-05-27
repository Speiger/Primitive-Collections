package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2ObjectMapGetTester<V> extends AbstractInt2ObjectMapTester<V>
{
	@CollectionSize.Require(absent = ZERO)
	public void testGet_yes() {
		assertEquals("get(present) should return the associated value", v0(), get(k0()));
	}
	
	public void testGet_no() {
		assertEquals("get(notPresent) should return null", null, get(k3()));
	}
}
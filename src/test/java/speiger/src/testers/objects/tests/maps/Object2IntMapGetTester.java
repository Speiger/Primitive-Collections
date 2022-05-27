package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2IntMapTester;

@Ignore
public class Object2IntMapGetTester<T> extends AbstractObject2IntMapTester<T>
{
	@CollectionSize.Require(absent = ZERO)
	public void testGet_yes() {
		assertEquals("get(present) should return the associated value", v0(), get(k0()));
	}
	
	public void testGet_no() {
		assertEquals("get(notPresent) should return -1", -1, get(k3()));
	}
}
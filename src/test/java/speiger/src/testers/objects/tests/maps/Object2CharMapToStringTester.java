package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.maps.impl.hash.Object2CharLinkedOpenHashMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2CharMapTester;

@Ignore
public class Object2CharMapToStringTester<T> extends AbstractObject2CharMapTester<T>
{
	public void testToString_minimal() {
		assertNotNull("toString() should not return null", getMap().toString());
	}

	@CollectionSize.Require(ZERO)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size0() {
		assertEquals("emptyMap.toString should return {}", "{}", getMap().toString());
	}

	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size1() {
		assertEquals("size1Map.toString should return {entry}", "{" + e0() + "}", getMap().toString());
	}

	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_formatting() {
		assertEquals("map.toString() incorrect", expectedToString(getMap().object2CharEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Object2CharMap.Entry<T>> entries) {
		Object2CharMap<T> reference = new Object2CharLinkedOpenHashMap<>();
		for (Object2CharMap.Entry<T> entry : entries) {
			reference.put(entry.getKey(), entry.getCharValue());
		}
		return reference.toString();
	}
}
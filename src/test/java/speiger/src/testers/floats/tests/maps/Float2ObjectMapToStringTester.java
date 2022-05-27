package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.maps.impl.hash.Float2ObjectLinkedOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ObjectMapToStringTester<V> extends AbstractFloat2ObjectMapTester<V>
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().float2ObjectEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Float2ObjectMap.Entry<V>> entries) {
		Float2ObjectMap<V> reference = new Float2ObjectLinkedOpenHashMap<>();
		for (Float2ObjectMap.Entry<V> entry : entries) {
			reference.put(entry.getFloatKey(), entry.getValue());
		}
		return reference.toString();
	}
}
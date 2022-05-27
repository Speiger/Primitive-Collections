package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2ObjectLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2ObjectMapToStringTester<V> extends AbstractShort2ObjectMapTester<V>
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().short2ObjectEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Short2ObjectMap.Entry<V>> entries) {
		Short2ObjectMap<V> reference = new Short2ObjectLinkedOpenHashMap<>();
		for (Short2ObjectMap.Entry<V> entry : entries) {
			reference.put(entry.getShortKey(), entry.getValue());
		}
		return reference.toString();
	}
}
package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2DoubleLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2DoubleMapTester;

@Ignore
public class Short2DoubleMapToStringTester extends AbstractShort2DoubleMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().short2DoubleEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Short2DoubleMap.Entry> entries) {
		Short2DoubleMap reference = new Short2DoubleLinkedOpenHashMap();
		for (Short2DoubleMap.Entry entry : entries) {
			reference.put(entry.getShortKey(), entry.getDoubleValue());
		}
		return reference.toString();
	}
}
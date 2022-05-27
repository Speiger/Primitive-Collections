package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2FloatLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2FloatMapTester;

@Ignore
public class Short2FloatMapToStringTester extends AbstractShort2FloatMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().short2FloatEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Short2FloatMap.Entry> entries) {
		Short2FloatMap reference = new Short2FloatLinkedOpenHashMap();
		for (Short2FloatMap.Entry entry : entries) {
			reference.put(entry.getShortKey(), entry.getFloatValue());
		}
		return reference.toString();
	}
}
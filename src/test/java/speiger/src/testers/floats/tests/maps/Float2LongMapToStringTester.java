package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.maps.impl.hash.Float2LongLinkedOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2LongMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2LongMapTester;

@Ignore
public class Float2LongMapToStringTester extends AbstractFloat2LongMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().float2LongEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Float2LongMap.Entry> entries) {
		Float2LongMap reference = new Float2LongLinkedOpenHashMap();
		for (Float2LongMap.Entry entry : entries) {
			reference.put(entry.getFloatKey(), entry.getLongValue());
		}
		return reference.toString();
	}
}
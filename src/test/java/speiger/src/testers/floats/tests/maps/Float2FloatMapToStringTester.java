package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.maps.impl.hash.Float2FloatLinkedOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2FloatMapToStringTester extends AbstractFloat2FloatMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().float2FloatEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Float2FloatMap.Entry> entries) {
		Float2FloatMap reference = new Float2FloatLinkedOpenHashMap();
		for (Float2FloatMap.Entry entry : entries) {
			reference.put(entry.getFloatKey(), entry.getFloatValue());
		}
		return reference.toString();
	}
}
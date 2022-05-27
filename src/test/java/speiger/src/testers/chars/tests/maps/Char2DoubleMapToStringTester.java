package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2DoubleLinkedOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2DoubleMapTester;

@Ignore
public class Char2DoubleMapToStringTester extends AbstractChar2DoubleMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().char2DoubleEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Char2DoubleMap.Entry> entries) {
		Char2DoubleMap reference = new Char2DoubleLinkedOpenHashMap();
		for (Char2DoubleMap.Entry entry : entries) {
			reference.put(entry.getCharKey(), entry.getDoubleValue());
		}
		return reference.toString();
	}
}
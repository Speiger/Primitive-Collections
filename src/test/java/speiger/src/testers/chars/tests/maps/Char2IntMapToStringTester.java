package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2IntLinkedOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2IntMapTester;

@Ignore
public class Char2IntMapToStringTester extends AbstractChar2IntMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().char2IntEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Char2IntMap.Entry> entries) {
		Char2IntMap reference = new Char2IntLinkedOpenHashMap();
		for (Char2IntMap.Entry entry : entries) {
			reference.put(entry.getCharKey(), entry.getIntValue());
		}
		return reference.toString();
	}
}
package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2CharLinkedOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2CharMapToStringTester extends AbstractChar2CharMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().char2CharEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Char2CharMap.Entry> entries) {
		Char2CharMap reference = new Char2CharLinkedOpenHashMap();
		for (Char2CharMap.Entry entry : entries) {
			reference.put(entry.getCharKey(), entry.getCharValue());
		}
		return reference.toString();
	}
}
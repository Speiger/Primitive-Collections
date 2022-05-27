package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.maps.impl.hash.Char2LongLinkedOpenHashMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2LongMapToStringTester extends AbstractChar2LongMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().char2LongEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Char2LongMap.Entry> entries) {
		Char2LongMap reference = new Char2LongLinkedOpenHashMap();
		for (Char2LongMap.Entry entry : entries) {
			reference.put(entry.getCharKey(), entry.getLongValue());
		}
		return reference.toString();
	}
}
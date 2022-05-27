package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestChar2BooleanMapGenerator extends TestMapGenerator<Character, Boolean> {
	public ObjectSamples<Char2BooleanMap.Entry> getSamples();
	public ObjectIterable<Char2BooleanMap.Entry> order(ObjectList<Char2BooleanMap.Entry> insertionOrder);
	public Char2BooleanMap create(Char2BooleanMap.Entry... elements);
	@Override
	default Char2BooleanMap create(Object... elements) {
		Char2BooleanMap.Entry[] result = new Char2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Char2BooleanMap.Entry[] createArray(int length) { return new Char2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Boolean>> samples() {
		ObjectSamples<Char2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getBooleanValue())
		);
	}
}
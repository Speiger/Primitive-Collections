package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestChar2CharMapGenerator extends TestMapGenerator<Character, Character> {
	public ObjectSamples<Char2CharMap.Entry> getSamples();
	public ObjectIterable<Char2CharMap.Entry> order(ObjectList<Char2CharMap.Entry> insertionOrder);
	public Char2CharMap create(Char2CharMap.Entry... elements);
	@Override
	default Char2CharMap create(Object... elements) {
		Char2CharMap.Entry[] result = new Char2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Char2CharMap.Entry[] createArray(int length) { return new Char2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Character>> samples() {
		ObjectSamples<Char2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getCharValue())
		);
	}
}
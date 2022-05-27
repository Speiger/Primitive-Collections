package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestChar2ShortMapGenerator extends TestMapGenerator<Character, Short> {
	public ObjectSamples<Char2ShortMap.Entry> getSamples();
	public ObjectIterable<Char2ShortMap.Entry> order(ObjectList<Char2ShortMap.Entry> insertionOrder);
	public Char2ShortMap create(Char2ShortMap.Entry... elements);
	@Override
	default Char2ShortMap create(Object... elements) {
		Char2ShortMap.Entry[] result = new Char2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Char2ShortMap.Entry[] createArray(int length) { return new Char2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Short>> samples() {
		ObjectSamples<Char2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getShortValue())
		);
	}
}
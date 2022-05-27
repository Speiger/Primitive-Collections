package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestChar2FloatMapGenerator extends TestMapGenerator<Character, Float> {
	public ObjectSamples<Char2FloatMap.Entry> getSamples();
	public ObjectIterable<Char2FloatMap.Entry> order(ObjectList<Char2FloatMap.Entry> insertionOrder);
	public Char2FloatMap create(Char2FloatMap.Entry... elements);
	@Override
	default Char2FloatMap create(Object... elements) {
		Char2FloatMap.Entry[] result = new Char2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Char2FloatMap.Entry[] createArray(int length) { return new Char2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Float>> samples() {
		ObjectSamples<Char2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getFloatValue())
		);
	}
}
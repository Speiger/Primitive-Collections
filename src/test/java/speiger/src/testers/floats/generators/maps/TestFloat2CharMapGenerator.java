package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestFloat2CharMapGenerator extends TestMapGenerator<Float, Character> {
	public ObjectSamples<Float2CharMap.Entry> getSamples();
	public ObjectIterable<Float2CharMap.Entry> order(ObjectList<Float2CharMap.Entry> insertionOrder);
	public Float2CharMap create(Float2CharMap.Entry... elements);
	@Override
	default Float2CharMap create(Object... elements) {
		Float2CharMap.Entry[] result = new Float2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Float2CharMap.Entry[] createArray(int length) { return new Float2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, Character>> samples() {
		ObjectSamples<Float2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getCharValue())
		);
	}
}
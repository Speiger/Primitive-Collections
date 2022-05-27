package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestChar2DoubleMapGenerator extends TestMapGenerator<Character, Double> {
	public ObjectSamples<Char2DoubleMap.Entry> getSamples();
	public ObjectIterable<Char2DoubleMap.Entry> order(ObjectList<Char2DoubleMap.Entry> insertionOrder);
	public Char2DoubleMap create(Char2DoubleMap.Entry... elements);
	@Override
	default Char2DoubleMap create(Object... elements) {
		Char2DoubleMap.Entry[] result = new Char2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Char2DoubleMap.Entry[] createArray(int length) { return new Char2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Double>> samples() {
		ObjectSamples<Char2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getDoubleValue())
		);
	}
}
package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestDouble2CharMapGenerator extends TestMapGenerator<Double, Character> {
	public ObjectSamples<Double2CharMap.Entry> getSamples();
	public ObjectIterable<Double2CharMap.Entry> order(ObjectList<Double2CharMap.Entry> insertionOrder);
	public Double2CharMap create(Double2CharMap.Entry... elements);
	@Override
	default Double2CharMap create(Object... elements) {
		Double2CharMap.Entry[] result = new Double2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Double2CharMap.Entry[] createArray(int length) { return new Double2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Character>> samples() {
		ObjectSamples<Double2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getCharValue())
		);
	}
}
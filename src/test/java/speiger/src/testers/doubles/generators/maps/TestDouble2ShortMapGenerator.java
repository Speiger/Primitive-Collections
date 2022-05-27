package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestDouble2ShortMapGenerator extends TestMapGenerator<Double, Short> {
	public ObjectSamples<Double2ShortMap.Entry> getSamples();
	public ObjectIterable<Double2ShortMap.Entry> order(ObjectList<Double2ShortMap.Entry> insertionOrder);
	public Double2ShortMap create(Double2ShortMap.Entry... elements);
	@Override
	default Double2ShortMap create(Object... elements) {
		Double2ShortMap.Entry[] result = new Double2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Double2ShortMap.Entry[] createArray(int length) { return new Double2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, Short>> samples() {
		ObjectSamples<Double2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getShortValue())
		);
	}
}
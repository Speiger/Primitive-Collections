package speiger.src.testers.doubles.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestDouble2ObjectMapGenerator<V> extends TestMapGenerator<Double, V> {
	public ObjectSamples<Double2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Double2ObjectMap.Entry<V>> order(ObjectList<Double2ObjectMap.Entry<V>> insertionOrder);
	public Double2ObjectMap<V> create(Double2ObjectMap.Entry<V>... elements);
	@Override
	default Double2ObjectMap<V> create(Object... elements) {
		Double2ObjectMap.Entry<V>[] result = new Double2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Double2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Double[] createKeyArray(int length) { return new Double[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Double2ObjectMap.Entry<V>[] createArray(int length) { return new Double2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Double, V>> samples() {
		ObjectSamples<Double2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getDoubleKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getDoubleKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getDoubleKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getDoubleKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getDoubleKey(), samples.e4().getValue())
		);
	}
}
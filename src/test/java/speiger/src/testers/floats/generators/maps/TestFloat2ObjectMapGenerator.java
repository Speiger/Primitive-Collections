package speiger.src.testers.floats.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestFloat2ObjectMapGenerator<V> extends TestMapGenerator<Float, V> {
	public ObjectSamples<Float2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Float2ObjectMap.Entry<V>> order(ObjectList<Float2ObjectMap.Entry<V>> insertionOrder);
	public Float2ObjectMap<V> create(Float2ObjectMap.Entry<V>... elements);
	@Override
	default Float2ObjectMap<V> create(Object... elements) {
		Float2ObjectMap.Entry<V>[] result = new Float2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Float2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Float[] createKeyArray(int length) { return new Float[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Float2ObjectMap.Entry<V>[] createArray(int length) { return new Float2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Float, V>> samples() {
		ObjectSamples<Float2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getFloatKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getFloatKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getFloatKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getFloatKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getFloatKey(), samples.e4().getValue())
		);
	}
}
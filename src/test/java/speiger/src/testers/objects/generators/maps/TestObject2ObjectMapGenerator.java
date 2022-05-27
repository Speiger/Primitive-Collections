package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestObject2ObjectMapGenerator<T, V> extends TestMapGenerator<T, V> {
	public ObjectSamples<Object2ObjectMap.Entry<T, V>> getSamples();
	public ObjectIterable<Object2ObjectMap.Entry<T, V>> order(ObjectList<Object2ObjectMap.Entry<T, V>> insertionOrder);
	public Object2ObjectMap<T, V> create(Object2ObjectMap.Entry<T, V>... elements);
	@Override
	default Object2ObjectMap<T, V> create(Object... elements) {
		Object2ObjectMap.Entry<T, V>[] result = new Object2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2ObjectMap.Entry<T, V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Object2ObjectMap.Entry<T, V>[] createArray(int length) { return new Object2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, V>> samples() {
		ObjectSamples<Object2ObjectMap.Entry<T, V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getValue())
		);
	}
}
package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestShort2ObjectMapGenerator<V> extends TestMapGenerator<Short, V> {
	public ObjectSamples<Short2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Short2ObjectMap.Entry<V>> order(ObjectList<Short2ObjectMap.Entry<V>> insertionOrder);
	public Short2ObjectMap<V> create(Short2ObjectMap.Entry<V>... elements);
	@Override
	default Short2ObjectMap<V> create(Object... elements) {
		Short2ObjectMap.Entry<V>[] result = new Short2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Short2ObjectMap.Entry<V>[] createArray(int length) { return new Short2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, V>> samples() {
		ObjectSamples<Short2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getValue())
		);
	}
}
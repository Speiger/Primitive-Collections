package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestLong2ObjectMapGenerator<V> extends TestMapGenerator<Long, V> {
	public ObjectSamples<Long2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Long2ObjectMap.Entry<V>> order(ObjectList<Long2ObjectMap.Entry<V>> insertionOrder);
	public Long2ObjectMap<V> create(Long2ObjectMap.Entry<V>... elements);
	@Override
	default Long2ObjectMap<V> create(Object... elements) {
		Long2ObjectMap.Entry<V>[] result = new Long2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Long2ObjectMap.Entry<V>[] createArray(int length) { return new Long2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, V>> samples() {
		ObjectSamples<Long2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getValue())
		);
	}
}
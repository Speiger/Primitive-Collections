package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestInt2ObjectMapGenerator<V> extends TestMapGenerator<Integer, V> {
	public ObjectSamples<Int2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Int2ObjectMap.Entry<V>> order(ObjectList<Int2ObjectMap.Entry<V>> insertionOrder);
	public Int2ObjectMap<V> create(Int2ObjectMap.Entry<V>... elements);
	@Override
	default Int2ObjectMap<V> create(Object... elements) {
		Int2ObjectMap.Entry<V>[] result = new Int2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Int2ObjectMap.Entry<V>[] createArray(int length) { return new Int2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, V>> samples() {
		ObjectSamples<Int2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getValue())
		);
	}
}
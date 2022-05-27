package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestByte2ObjectMapGenerator<V> extends TestMapGenerator<Byte, V> {
	public ObjectSamples<Byte2ObjectMap.Entry<V>> getSamples();
	public ObjectIterable<Byte2ObjectMap.Entry<V>> order(ObjectList<Byte2ObjectMap.Entry<V>> insertionOrder);
	public Byte2ObjectMap<V> create(Byte2ObjectMap.Entry<V>... elements);
	@Override
	default Byte2ObjectMap<V> create(Object... elements) {
		Byte2ObjectMap.Entry<V>[] result = new Byte2ObjectMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2ObjectMap.Entry<V>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default V[] createValueArray(int length) { return (V[])new Object[length]; }
	@Override
	default Byte2ObjectMap.Entry<V>[] createArray(int length) { return new Byte2ObjectMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, V>> samples() {
		ObjectSamples<Byte2ObjectMap.Entry<V>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getValue())
		);
	}
}
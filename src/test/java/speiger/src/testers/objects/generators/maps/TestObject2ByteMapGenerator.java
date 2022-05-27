package speiger.src.testers.objects.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestObject2ByteMapGenerator<T> extends TestMapGenerator<T, Byte> {
	public ObjectSamples<Object2ByteMap.Entry<T>> getSamples();
	public ObjectIterable<Object2ByteMap.Entry<T>> order(ObjectList<Object2ByteMap.Entry<T>> insertionOrder);
	public Object2ByteMap<T> create(Object2ByteMap.Entry<T>... elements);
	@Override
	default Object2ByteMap<T> create(Object... elements) {
		Object2ByteMap.Entry<T>[] result = new Object2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Object2ByteMap.Entry<T>) elements[i];
		}
		return create(result);
	}
	
	@Override
	default T[] createKeyArray(int length) { return (T[])new Object[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Object2ByteMap.Entry<T>[] createArray(int length) { return new Object2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<T, Byte>> samples() {
		ObjectSamples<Object2ByteMap.Entry<T>> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getKey(), samples.e4().getByteValue())
		);
	}
}
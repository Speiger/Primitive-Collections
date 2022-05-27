package speiger.src.testers.objects.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestObjectCollectionGenerator<T> extends TestCollectionGenerator<T>
{
	public ObjectSamples<T> getSamples();
	public ObjectIterable<T> order(ObjectList<T> insertionOrder);
	
	@Override
	public default SampleElements<T> samples() {return getSamples().toSamples();}
	@Override
	public ObjectCollection<T> create(Object... elements);
	@Override
	public default T[] createArray(int length) { return (T[])new Object[length]; }
	@Override
	public Iterable<T> order(List<T> insertionOrder);
}
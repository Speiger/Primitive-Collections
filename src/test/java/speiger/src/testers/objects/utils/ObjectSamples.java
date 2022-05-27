package speiger.src.testers.objects.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;

@SuppressWarnings("javadoc")
public class ObjectSamples<T> implements ObjectIterable<T>
{
	private final T e0;
	private final T e1;
	private final T e2;
	private final T e3;
	private final T e4;
	
	public ObjectSamples(SampleElements<T> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public ObjectSamples(T e0, T e1, T e2, T e3, T e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<T> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public ObjectIterator<T> iterator()
	{
		return asList().iterator();
	}
	
	public ObjectList<T> asList()
	{
		return ObjectArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public T e0()
	{
		return e0;
	}
	
	public T e1()
	{
		return e1;
	}
	
	public T e2()
	{
		return e2;
	}
	
	public T e3()
	{
		return e3;
	}
	
	public T e4()
	{
		return e4;
	}
}
package speiger.src.testers.shorts.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;

@SuppressWarnings("javadoc")
public class ShortSamples implements ShortIterable
{
	private final short e0;
	private final short e1;
	private final short e2;
	private final short e3;
	private final short e4;
	
	public ShortSamples(SampleElements<Short> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public ShortSamples(short e0, short e1, short e2, short e3, short e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Short> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public ShortIterator iterator()
	{
		return asList().iterator();
	}
	
	public ShortList asList()
	{
		return ShortArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public short e0()
	{
		return e0;
	}
	
	public short e1()
	{
		return e1;
	}
	
	public short e2()
	{
		return e2;
	}
	
	public short e3()
	{
		return e3;
	}
	
	public short e4()
	{
		return e4;
	}
}
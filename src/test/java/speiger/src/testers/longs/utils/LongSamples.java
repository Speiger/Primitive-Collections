package speiger.src.testers.longs.utils;

import com.google.common.collect.testing.SampleElements;

import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;

public class LongSamples implements LongIterable
{
	private final long e0;
	private final long e1;
	private final long e2;
	private final long e3;
	private final long e4;
	
	public LongSamples(SampleElements<Long> samples)
	{
		this(samples.e0(), samples.e1(), samples.e2(), samples.e3(), samples.e4());
	}
	
	public LongSamples(long e0, long e1, long e2, long e3, long e4)
	{
		this.e0 = e0;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}
	
	public SampleElements<Long> toSamples()
	{
		return new SampleElements<>(e0(), e1(), e2(), e3(), e4());
	}
	
	@Override
	public LongIterator iterator()
	{
		return asList().iterator();
	}
	
	public LongList asList()
	{
		return LongArrayList.wrap(e0(), e1(), e2(), e3(), e4());
	}
	
	public long e0()
	{
		return e0;
	}
	
	public long e1()
	{
		return e1;
	}
	
	public long e2()
	{
		return e2;
	}
	
	public long e3()
	{
		return e3;
	}
	
	public long e4()
	{
		return e4;
	}
}
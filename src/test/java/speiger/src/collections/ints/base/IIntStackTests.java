package speiger.src.collections.ints.base;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.ints.collections.IntStack;

public interface IIntStackTests
{
	static final int[] TEST_ARRAY = IntStream.range(0, 100).toArray();

	public IntStack create(int[] data);
	
	@Test
	public default void testPush()
	{
		IntStack stacks = create(TEST_ARRAY);
		stacks.pushInt(500);
		Assert.assertEquals(500, stacks.topInt());
	}
	
	@Test
	public default void testPop()
	{
		Assert.assertEquals(99, create(TEST_ARRAY).topInt());
	}
}

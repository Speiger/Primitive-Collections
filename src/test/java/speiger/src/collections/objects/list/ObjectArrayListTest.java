package speiger.src.collections.objects.list;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.utils.IObjectArray;

@SuppressWarnings("javadoc")
public class ObjectArrayListTest
{
	@Test
	public void testCastable()
	{
		ObjectArrayList<String> list = new ObjectArrayList<>();
		list.add("Test");
		list.add("Testing");
		list.add("Testing stuff");
		testCastable(list, false);
		testCastable(ObjectArrayList.wrap("Test", "Testing", "Testing stuff"), true);
	}
	
	public <T> void testCastable(IObjectArray<T> castable, boolean result)
	{
		Assert.assertTrue(castable.isCastable() == result);
		AtomicInteger amount = new AtomicInteger();
		castable.elements(T -> amount.set(T.length));
		Assert.assertTrue(amount.get() > 0 == result);
		
	}
}

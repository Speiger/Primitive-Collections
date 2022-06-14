package speiger.src.testers.base.tests.list;

import static com.google.common.collect.testing.IteratorFeature.MODIFIABLE;
import static com.google.common.collect.testing.IteratorFeature.UNMODIFIABLE;
import static java.util.Collections.singleton;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.IteratorFeature;
import com.google.common.collect.testing.ListIteratorTester;
import com.google.common.collect.testing.testers.AbstractListTester;

import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class JavaListListIteratorTester<E> extends AbstractListTester<E>
{
	@SpecialFeature.Require(absent = SpecialFeature.ITERATOR_MODIFIABLE)
	public void testListIterator_unmodifiable()
	{
		runListIteratorTest(UNMODIFIABLE);
	}
	
	@SpecialFeature.Require(SpecialFeature.ITERATOR_MODIFIABLE)
	public void testListIterator_fullyModifiable()
	{
		runListIteratorTest(MODIFIABLE);
	}
	
	private void runListIteratorTest(Set<IteratorFeature> features)
	{
		new ListIteratorTester<E>(4, singleton(e4()), features, Helpers.copyToList(getOrderedElements()), 0)
		{
			@Override
			protected ListIterator<E> newTargetIterator()
			{
				resetCollection();
				return getList().listIterator();
			}
			
			@Override
			protected void verify(List<E> elements)
			{
				expectContents(elements);
			}
		}.test();
	}
	
	public void testListIterator_tooLow()
	{
		try
		{
			getList().listIterator(-1);
			fail();
		}
		catch(IndexOutOfBoundsException expected)
		{
		}
	}
	
	public void testListIterator_tooHigh()
	{
		try
		{
			getList().listIterator(getNumElements() + 1);
			fail();
		}
		catch(IndexOutOfBoundsException expected)
		{
		}
	}
	
	public void testListIterator_atSize()
	{
		getList().listIterator(getNumElements());
		// TODO: run the iterator through ListIteratorTester
	}
}

package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ShortMapIsEmptyTester<T> extends AbstractObject2ShortMapTester<T> 
{
	  @CollectionSize.Require(ZERO)
	  public void testIsEmpty_yes() {
	    assertTrue("isEmpty() should return true", getMap().isEmpty());
	  }

	  @CollectionSize.Require(absent = ZERO)
	  public void testIsEmpty_no() {
	    assertFalse("isEmpty() should return false", getMap().isEmpty());
	  }
}
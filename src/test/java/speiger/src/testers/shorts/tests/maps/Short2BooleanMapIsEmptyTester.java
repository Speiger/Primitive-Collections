package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2BooleanMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2BooleanMapIsEmptyTester extends AbstractShort2BooleanMapTester 
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
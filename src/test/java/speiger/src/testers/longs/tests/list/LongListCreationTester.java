package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.REJECTS_DUPLICATES_AT_CREATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.AbstractLongListTester;

@Ignore
public class LongListCreationTester extends AbstractLongListTester
{
	@CollectionFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates() {
		long[] array = createSamplesArray();
		array[1] = e0();
		collection = primitiveGenerator.create(array);
		expectContents(array);
	}
}
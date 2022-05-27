package speiger.src.testers.shorts.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.shorts.maps.interfaces.Short2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2BooleanMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2BooleanMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Short2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Boolean> {
	public static Short2BooleanMapTestSuiteBuilder using(TestShort2BooleanMapGenerator generator) {
		return (Short2BooleanMapTestSuiteBuilder) new Short2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2BooleanMapClearTester.class);
		testers.add(Short2BooleanMapComputeTester.class);
		testers.add(Short2BooleanMapComputeIfAbsentTester.class);
		testers.add(Short2BooleanMapComputeIfPresentTester.class);
		testers.add(Short2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Short2BooleanMapContainsKeyTester.class);
		testers.add(Short2BooleanMapContainsValueTester.class);
		testers.add(Short2BooleanMapEntrySetTester.class);
		testers.add(Short2BooleanMapEqualsTester.class);
		testers.add(Short2BooleanMapForEachTester.class);
		testers.add(Short2BooleanMapGetTester.class);
		testers.add(Short2BooleanMapGetOrDefaultTester.class);
		testers.add(Short2BooleanMapHashCodeTester.class);
		testers.add(Short2BooleanMapIsEmptyTester.class);
		testers.add(Short2BooleanMapMergeTester.class);
		testers.add(Short2BooleanMapPutTester.class);
		testers.add(Short2BooleanMapPutAllTester.class);
		testers.add(Short2BooleanMapPutAllArrayTester.class);
		testers.add(Short2BooleanMapPutIfAbsentTester.class);
		testers.add(Short2BooleanMapRemoveTester.class);
		testers.add(Short2BooleanMapRemoveEntryTester.class);
		testers.add(Short2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Short2BooleanMapReplaceTester.class);
		testers.add(Short2BooleanMapReplaceAllTester.class);
		testers.add(Short2BooleanMapReplaceEntryTester.class);
		testers.add(Short2BooleanMapSizeTester.class);
		testers.add(Short2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Boolean>, Map.Entry<Short, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
	}
	
	protected BooleanCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestBooleanCollectionGenerator generator) {
		return BooleanCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}
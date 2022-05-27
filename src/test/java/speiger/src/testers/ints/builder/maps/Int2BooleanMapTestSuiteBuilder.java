package speiger.src.testers.ints.builder.maps;

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
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2BooleanMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2BooleanMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapClearTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapComputeIfPresentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapComputeTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapContainsKeyTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapContainsValueTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapEntrySetTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapEqualsTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapForEachTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapGetOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapGetTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapHashCodeTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapIsEmptyTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapMergeTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapPutAllArrayTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapPutAllTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapPutIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapPutTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapRemoveEntryTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapRemoveTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapReplaceAllTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapReplaceEntryTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapReplaceTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapSizeTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.ints.tests.maps.Int2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Int2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Integer, Boolean> {
	public static Int2BooleanMapTestSuiteBuilder using(TestInt2BooleanMapGenerator generator) {
		return (Int2BooleanMapTestSuiteBuilder) new Int2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Int2BooleanMapClearTester.class);
		testers.add(Int2BooleanMapComputeTester.class);
		testers.add(Int2BooleanMapComputeIfAbsentTester.class);
		testers.add(Int2BooleanMapComputeIfPresentTester.class);
		testers.add(Int2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Int2BooleanMapContainsKeyTester.class);
		testers.add(Int2BooleanMapContainsValueTester.class);
		testers.add(Int2BooleanMapEntrySetTester.class);
		testers.add(Int2BooleanMapEqualsTester.class);
		testers.add(Int2BooleanMapForEachTester.class);
		testers.add(Int2BooleanMapGetTester.class);
		testers.add(Int2BooleanMapGetOrDefaultTester.class);
		testers.add(Int2BooleanMapHashCodeTester.class);
		testers.add(Int2BooleanMapIsEmptyTester.class);
		testers.add(Int2BooleanMapMergeTester.class);
		testers.add(Int2BooleanMapPutTester.class);
		testers.add(Int2BooleanMapPutAllTester.class);
		testers.add(Int2BooleanMapPutAllArrayTester.class);
		testers.add(Int2BooleanMapPutIfAbsentTester.class);
		testers.add(Int2BooleanMapRemoveTester.class);
		testers.add(Int2BooleanMapRemoveEntryTester.class);
		testers.add(Int2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Int2BooleanMapReplaceTester.class);
		testers.add(Int2BooleanMapReplaceAllTester.class);
		testers.add(Int2BooleanMapReplaceEntryTester.class);
		testers.add(Int2BooleanMapSizeTester.class);
		testers.add(Int2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Boolean>, Map.Entry<Integer, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedInt2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedInt2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Int2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Int2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected IntSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator generator) {
		return IntSetTestSuiteBuilder.using(generator);
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
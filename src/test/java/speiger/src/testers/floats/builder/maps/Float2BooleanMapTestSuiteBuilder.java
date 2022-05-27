package speiger.src.testers.floats.builder.maps;

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
import speiger.src.collections.floats.maps.interfaces.Float2BooleanMap;
import speiger.src.testers.booleans.builder.BooleanCollectionTestSuiteBuilder;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.generators.TestFloatSetGenerator;
import speiger.src.testers.floats.generators.maps.TestFloat2BooleanMapGenerator;
import speiger.src.testers.floats.impl.maps.DerivedFloat2BooleanMapGenerators;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapClearTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapComputeIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapComputeIfPresentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapComputeTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapContainsKeyTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapContainsValueTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapEntrySetTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapEqualsTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapForEachTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapGetOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapGetTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapHashCodeTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapIsEmptyTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapMergeTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapPutAllArrayTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapPutAllTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapPutIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapPutTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapRemoveEntryTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapRemoveOrDefaultTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapRemoveTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapReplaceAllTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapReplaceEntryTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapReplaceTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapSizeTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapSupplyIfAbsentTester;
import speiger.src.testers.floats.tests.maps.Float2BooleanMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Float2BooleanMapTestSuiteBuilder extends MapTestSuiteBuilder<Float, Boolean> {
	public static Float2BooleanMapTestSuiteBuilder using(TestFloat2BooleanMapGenerator generator) {
		return (Float2BooleanMapTestSuiteBuilder) new Float2BooleanMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Float2BooleanMapClearTester.class);
		testers.add(Float2BooleanMapComputeTester.class);
		testers.add(Float2BooleanMapComputeIfAbsentTester.class);
		testers.add(Float2BooleanMapComputeIfPresentTester.class);
		testers.add(Float2BooleanMapSupplyIfAbsentTester.class);
		testers.add(Float2BooleanMapContainsKeyTester.class);
		testers.add(Float2BooleanMapContainsValueTester.class);
		testers.add(Float2BooleanMapEntrySetTester.class);
		testers.add(Float2BooleanMapEqualsTester.class);
		testers.add(Float2BooleanMapForEachTester.class);
		testers.add(Float2BooleanMapGetTester.class);
		testers.add(Float2BooleanMapGetOrDefaultTester.class);
		testers.add(Float2BooleanMapHashCodeTester.class);
		testers.add(Float2BooleanMapIsEmptyTester.class);
		testers.add(Float2BooleanMapMergeTester.class);
		testers.add(Float2BooleanMapPutTester.class);
		testers.add(Float2BooleanMapPutAllTester.class);
		testers.add(Float2BooleanMapPutAllArrayTester.class);
		testers.add(Float2BooleanMapPutIfAbsentTester.class);
		testers.add(Float2BooleanMapRemoveTester.class);
		testers.add(Float2BooleanMapRemoveEntryTester.class);
		testers.add(Float2BooleanMapRemoveOrDefaultTester.class);
		testers.add(Float2BooleanMapReplaceTester.class);
		testers.add(Float2BooleanMapReplaceAllTester.class);
		testers.add(Float2BooleanMapReplaceEntryTester.class);
		testers.add(Float2BooleanMapSizeTester.class);
		testers.add(Float2BooleanMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Float, Boolean>, Map.Entry<Float, Boolean>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedFloat2BooleanMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedFloat2BooleanMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Float2BooleanMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Float2BooleanMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected FloatSetTestSuiteBuilder createDerivedKeySetSuite(TestFloatSetGenerator generator) {
		return FloatSetTestSuiteBuilder.using(generator);
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
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_unknownOrderRemoveSupported");
		return suppressing;
	}
}
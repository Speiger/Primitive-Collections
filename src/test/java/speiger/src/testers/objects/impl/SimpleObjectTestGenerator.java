package speiger.src.testers.objects.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.objects.generators.TestObjectListGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.TestObjectOrderedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObjectTestGenerator<T, E extends ObjectCollection<T>> {
	Function<T[], E> mapper;
	ObjectSamples<T> elements;
	
	public SimpleObjectTestGenerator(Function<T[], E> mapper, ObjectSamples<T> elements) {
		this.mapper = mapper;
		this.elements = elements;
	}

	public ObjectSamples<T> getSamples() {
		return elements;
	}
	
	public E create(Object... elements) {
		T[] array = (T[])ObjectArrays.newArray(this.elements.e0().getClass(), elements.length);
		int i = 0;
		for (Object e : elements) {
			array[i++] = (T)e;
		}
		return mapper.apply(array);
	}

	public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<T> order(List<T> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections<T> extends SimpleObjectTestGenerator<T, ObjectCollection<T>> implements TestObjectCollectionGenerator<T>
	{
		public Collections(Function<T[], ObjectCollection<T>> mapper, ObjectSamples<T> samples) {
			super(mapper, samples);
		}
	}
	
	public static class Lists<T> extends SimpleObjectTestGenerator<T, ObjectList<T>> implements TestObjectListGenerator<T>
	{
		public Lists(Function<T[], ObjectList<T>> mapper, ObjectSamples<T> samples) {
			super(mapper, samples);
		}
	}
	
	public static class Sets<T> extends SimpleObjectTestGenerator<T, ObjectSet<T>> implements TestObjectSetGenerator<T>
	{
		public Sets(Function<T[], ObjectSet<T>> mapper, ObjectSamples<T> samples) {
			super(mapper, samples);
		}
	}
	
	public static class OrderedSets<T> extends SimpleObjectTestGenerator<T, ObjectOrderedSet<T>> implements TestObjectOrderedSetGenerator<T>
	{
		public OrderedSets(Function<T[], ObjectOrderedSet<T>> mapper, ObjectSamples<T> samples) {
			super(mapper, samples);
		}
	}
	
	public static class SortedSets<T> extends SimpleObjectTestGenerator<T, ObjectSortedSet<T>> implements TestObjectSortedSetGenerator<T>
	{
		T belowLesser;
		T belowGreater;
		T aboveLesser;
		T aboveGreater;
		public SortedSets(Function<T[], ObjectSortedSet<T>> mapper, ObjectSamples<T> samples, T belowLesser, T belowGreater, T aboveLesser, T aboveGreater) {
			super(mapper, samples);
			this.belowLesser = belowLesser;
			this.belowGreater = belowGreater;
			this.aboveLesser = aboveLesser;
			this.aboveGreater = aboveGreater;
		}
		
		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public T belowSamplesLesser() { return belowLesser; }
		@Override
		public T belowSamplesGreater() { return belowGreater; }
		@Override
		public T aboveSamplesLesser() { return aboveLesser; }
		@Override
		public T aboveSamplesGreater() { return aboveGreater; }
	}
	
	public static class NavigableSets<T> extends SimpleObjectTestGenerator<T, ObjectNavigableSet<T>> implements TestObjectNavigableSetGenerator<T>
	{
		T belowLesser;
		T belowGreater;
		T aboveLesser;
		T aboveGreater;
		public NavigableSets(Function<T[], ObjectNavigableSet<T>> mapper, ObjectSamples<T> samples, T belowLesser, T belowGreater, T aboveLesser, T aboveGreater) {
			super(mapper, samples);
			this.belowLesser = belowLesser;
			this.belowGreater = belowGreater;
			this.aboveLesser = aboveLesser;
			this.aboveGreater = aboveGreater;
		}
		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public T belowSamplesLesser() { return belowLesser; }
		@Override
		public T belowSamplesGreater() { return belowGreater; }
		@Override
		public T aboveSamplesLesser() { return aboveLesser; }
		@Override
		public T aboveSamplesGreater() { return aboveGreater; }
	}
}
package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanSortedMap;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2BooleanMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2BooleanSortedMapGenerator;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.booleans.utils.BooleanSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedObject2BooleanMapGenerators {
	public static class NavigableMapGenerator<T> extends SortedMapGenerator<T> {
		public NavigableMapGenerator(TestObject2BooleanSortedMapGenerator<T> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Object2BooleanNavigableMap<T> createSubMap(Object2BooleanSortedMap<T> sortedMap, T firstExclusive, T lastExclusive) {
	    	Object2BooleanNavigableMap<T> map = (Object2BooleanNavigableMap<T>) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Object2BooleanNavigableMap<T>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<T> extends MapGenerator<T> implements TestObject2BooleanSortedMapGenerator<T> {
		TestObject2BooleanSortedMapGenerator<T> parent;
		Bound to;
		Bound from;
		T firstInclusive;
		T lastInclusive;
		Comparator<Entry<T>> entryComparator;

		public SortedMapGenerator(TestObject2BooleanSortedMapGenerator<T> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Object2BooleanSortedMap<T> map = parent.create();
			entryComparator = DerivedObject2BooleanMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<T>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getKey();
			lastInclusive = samples.get(samples.size() - 1).getKey();
		}
		
		@Override
		public Object2BooleanSortedMap<T> create(Entry<T>... elements) {
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getKey(), parent.aboveSamplesLesser().getKey());
		}
		
		Object2BooleanSortedMap<T> createSubMap(Object2BooleanSortedMap<T> map, T firstExclusive, T lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry<T> belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T> belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T> aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry<T> aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator<T> extends MapGenerator<T> implements TestObject2BooleanSortedMapGenerator<T>
	{
		TestObject2BooleanSortedMapGenerator<T> parent;
		
		public DescendingTestMapGenerator(TestObject2BooleanSortedMapGenerator<T> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Object2BooleanNavigableMap<T> create(Entry<T>... elements) {
			return ((Object2BooleanNavigableMap<T>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<T, Boolean>> order(List<Map.Entry<T, Boolean>> insertionOrder) {
			ObjectList<Map.Entry<T, Boolean>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry<T>> order(ObjectList<Entry<T>> insertionOrder) {
			ObjectList<Entry<T>> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry<T> belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry<T> belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry<T> aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry<T> aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator<T> implements TestObject2BooleanMapGenerator<T>
	{
		TestObject2BooleanMapGenerator<T> parent;
		
		public MapGenerator(TestObject2BooleanMapGenerator<T> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<T, Boolean>> order(List<Map.Entry<T, Boolean>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry<T>> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry<T>> order(ObjectList<Entry<T>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Object2BooleanMap<T> create(Entry<T>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<T> implements TestObjectSetGenerator<Object2BooleanMap.Entry<T>> {
		TestObject2BooleanMapGenerator<T> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>> inner) {
			generator = (TestObject2BooleanMapGenerator<T>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry<T>> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry<T>> order(ObjectList<Entry<T>> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry<T>> order(List<Entry<T>> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry<T>>(insertionOrder));
		}

		@Override
		public Entry<T>[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry<T>> create(Object... elements) {
			return generator.create(elements).object2BooleanEntrySet();
		}
	}

	public static <T> TestObjectSetGenerator<T> keySetGenerator(OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>> inner) {
		if (inner.getInnerGenerator() instanceof TestObject2BooleanSortedMapGenerator) {
			ObjectSet<T> set = ((TestObject2BooleanSortedMapGenerator<T>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ObjectNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof ObjectSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<T> extends MapSortedKeySetGenerator<T> implements TestObjectNavigableSetGenerator<T> {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>> inner) {
			super(inner);
		}
		
		@Override
		public ObjectNavigableSet<T> create(Object... elements) {
			return (ObjectNavigableSet<T>) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<T> extends MapKeySetGenerator<T> implements TestObjectSortedSetGenerator<T> {
		TestObject2BooleanSortedMapGenerator<T> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>> inner) {
			super(inner);
			generator = (TestObject2BooleanSortedMapGenerator<T>) inner.getInnerGenerator();
		}

		@Override
		public ObjectSortedSet<T> create(Object... elements) {
			return (ObjectSortedSet<T>) super.create(elements);
		}

		@Override
		public T belowSamplesLesser() {
			return generator.belowSamplesLesser().getKey();
		}

		@Override
		public T belowSamplesGreater() {
			return generator.belowSamplesGreater().getKey();
		}

		@Override
		public T aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getKey();
		}

		@Override
		public T aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getKey();
		}
	}

	public static class MapKeySetGenerator<T> implements TestObjectSetGenerator<T> {
		TestObject2BooleanMapGenerator<T> generator;
		ObjectSamples<T> samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>> inner) {
			generator = (TestObject2BooleanMapGenerator<T>) inner.getInnerGenerator();
			ObjectSamples<Object2BooleanMap.Entry<T>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getKey(), samples.e1().getKey(), samples.e2().getKey(), samples.e3().getKey(), samples.e4().getKey());
		}

		@Override
		public ObjectSamples<T> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			boolean value = generator.getSamples().e0().getBooleanValue();
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2BooleanMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}

		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			boolean value = generator.getSamples().e0().getBooleanValue();
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2BooleanMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}
		
		@Override
		public ObjectSet<T> create(Object... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2BooleanMap.BasicEntry<>((T) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<T> implements TestBooleanCollectionGenerator {
		TestObject2BooleanMapGenerator<T> generator;
		BooleanSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<T, Boolean>, Map.Entry<T, Boolean>> inner) {
			generator = (TestObject2BooleanMapGenerator<T>) inner.getInnerGenerator();
			ObjectSamples<Object2BooleanMap.Entry<T>> samples = generator.getSamples();
			this.samples = new BooleanSamples(samples.e0().getBooleanValue(), samples.e1().getBooleanValue(), samples.e2().getBooleanValue(), samples.e3().getBooleanValue(), samples.e4().getBooleanValue());
		}
		
		@Override
		public BooleanSamples getSamples() {
			return samples;
		}

		@Override
		public BooleanIterable order(BooleanList insertionOrder) {
			ObjectList<Entry<T>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new BooleanComparator() {
				@Override
				public int compare(boolean key, boolean value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(boolean entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getBooleanValue() == entry) return i;
					}
					throw new IllegalArgumentException("Object2BooleanMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Boolean> order(List<Boolean> insertionOrder) {
			ObjectList<Entry<T>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Boolean>() {
				@Override
				public int compare(Boolean key, Boolean value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Boolean entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getBooleanValue() == entry.booleanValue()) return i;
					}
					throw new IllegalArgumentException("Object2BooleanMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public BooleanCollection create(boolean... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2BooleanMap.BasicEntry<>(entry.getKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public BooleanCollection create(Object... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2BooleanMap.BasicEntry<>(entry.getKey(), (Boolean)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<T> Comparator<Map.Entry<T, Boolean>> entryObjectComparator(Comparator<T> keyComparator) {
		return new Comparator<Map.Entry<T, Boolean>>() {
			@Override
			public int compare(Map.Entry<T, Boolean> a, Map.Entry<T, Boolean> b) {
				if(keyComparator == null) {
					return ((Comparable<T>)a.getKey()).compareTo((T)b.getKey());
				}
				return keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
	
	public static<T> Comparator<Entry<T>> entryComparator(Comparator<T> keyComparator) {
		return new Comparator<Entry<T>>() {
			@Override
			public int compare(Entry<T> a, Entry<T> b) {
				if(keyComparator == null) {
					return ((Comparable<T>)a.getKey()).compareTo((T)b.getKey());
				}
				return keyComparator.compare(a.getKey(), b.getKey());
			}
		};
	}
}
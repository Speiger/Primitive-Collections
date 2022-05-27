package speiger.src.testers.objects.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap.Entry;
import speiger.src.collections.objects.maps.interfaces.Object2CharNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharSortedMap;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2CharMapGenerator;
import speiger.src.testers.objects.generators.maps.TestObject2CharSortedMapGenerator;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterable;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.chars.utils.CharSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedObject2CharMapGenerators {
	public static class NavigableMapGenerator<T> extends SortedMapGenerator<T> {
		public NavigableMapGenerator(TestObject2CharSortedMapGenerator<T> parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Object2CharNavigableMap<T> createSubMap(Object2CharSortedMap<T> sortedMap, T firstExclusive, T lastExclusive) {
	    	Object2CharNavigableMap<T> map = (Object2CharNavigableMap<T>) sortedMap;
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
	        return (Object2CharNavigableMap<T>) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator<T> extends MapGenerator<T> implements TestObject2CharSortedMapGenerator<T> {
		TestObject2CharSortedMapGenerator<T> parent;
		Bound to;
		Bound from;
		T firstInclusive;
		T lastInclusive;
		Comparator<Entry<T>> entryComparator;

		public SortedMapGenerator(TestObject2CharSortedMapGenerator<T> parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Object2CharSortedMap<T> map = parent.create();
			entryComparator = DerivedObject2CharMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry<T>> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getKey();
			lastInclusive = samples.get(samples.size() - 1).getKey();
		}
		
		@Override
		public Object2CharSortedMap<T> create(Entry<T>... elements) {
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
		
		Object2CharSortedMap<T> createSubMap(Object2CharSortedMap<T> map, T firstExclusive, T lastExclusive) {
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
	
	public static class DescendingTestMapGenerator<T> extends MapGenerator<T> implements TestObject2CharSortedMapGenerator<T>
	{
		TestObject2CharSortedMapGenerator<T> parent;
		
		public DescendingTestMapGenerator(TestObject2CharSortedMapGenerator<T> parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Object2CharNavigableMap<T> create(Entry<T>... elements) {
			return ((Object2CharNavigableMap<T>)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<T, Character>> order(List<Map.Entry<T, Character>> insertionOrder) {
			ObjectList<Map.Entry<T, Character>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator<T> implements TestObject2CharMapGenerator<T>
	{
		TestObject2CharMapGenerator<T> parent;
		
		public MapGenerator(TestObject2CharMapGenerator<T> parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<T, Character>> order(List<Map.Entry<T, Character>> insertionOrder) {
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
		public Object2CharMap<T> create(Entry<T>... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator<T> implements TestObjectSetGenerator<Object2CharMap.Entry<T>> {
		TestObject2CharMapGenerator<T> generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>> inner) {
			generator = (TestObject2CharMapGenerator<T>) inner.getInnerGenerator();
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
			return generator.create(elements).object2CharEntrySet();
		}
	}

	public static <T> TestObjectSetGenerator<T> keySetGenerator(OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>> inner) {
		if (inner.getInnerGenerator() instanceof TestObject2CharSortedMapGenerator) {
			ObjectSet<T> set = ((TestObject2CharSortedMapGenerator<T>) inner.getInnerGenerator()).create().keySet();
			if(set instanceof ObjectNavigableSet) return new MapNavigableKeySetGenerator<>(inner);
			if(set instanceof ObjectSortedSet) return new MapSortedKeySetGenerator<>(inner);
		}
		return new MapKeySetGenerator<>(inner);
	}
	
	public static class MapNavigableKeySetGenerator<T> extends MapSortedKeySetGenerator<T> implements TestObjectNavigableSetGenerator<T> {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>> inner) {
			super(inner);
		}
		
		@Override
		public ObjectNavigableSet<T> create(Object... elements) {
			return (ObjectNavigableSet<T>) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator<T> extends MapKeySetGenerator<T> implements TestObjectSortedSetGenerator<T> {
		TestObject2CharSortedMapGenerator<T> generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>> inner) {
			super(inner);
			generator = (TestObject2CharSortedMapGenerator<T>) inner.getInnerGenerator();
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
		TestObject2CharMapGenerator<T> generator;
		ObjectSamples<T> samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>> inner) {
			generator = (TestObject2CharMapGenerator<T>) inner.getInnerGenerator();
			ObjectSamples<Object2CharMap.Entry<T>> samples = generator.getSamples();
			this.samples = new ObjectSamples<>(samples.e0().getKey(), samples.e1().getKey(), samples.e2().getKey(), samples.e3().getKey(), samples.e4().getKey());
		}

		@Override
		public ObjectSamples<T> getSamples() {
			return samples;
		}

		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			char value = generator.getSamples().e0().getCharValue();
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2CharMap.BasicEntry<>(key, value));
			}
			ObjectList<T> list = new ObjectArrayList<>();
			for (Entry<T> entry : generator.order(entries)) {
				list.add(entry.getKey());
			}
			return list;
		}

		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			char value = generator.getSamples().e0().getCharValue();
			ObjectList<Entry<T>> entries = new ObjectArrayList<>();
			for (T key : insertionOrder) {
				entries.add(new AbstractObject2CharMap.BasicEntry<>(key, value));
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
				result[index] = new AbstractObject2CharMap.BasicEntry<>((T) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator<T> implements TestCharCollectionGenerator {
		TestObject2CharMapGenerator<T> generator;
		CharSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<T, Character>, Map.Entry<T, Character>> inner) {
			generator = (TestObject2CharMapGenerator<T>) inner.getInnerGenerator();
			ObjectSamples<Object2CharMap.Entry<T>> samples = generator.getSamples();
			this.samples = new CharSamples(samples.e0().getCharValue(), samples.e1().getCharValue(), samples.e2().getCharValue(), samples.e3().getCharValue(), samples.e4().getCharValue());
		}
		
		@Override
		public CharSamples getSamples() {
			return samples;
		}

		@Override
		public CharIterable order(CharList insertionOrder) {
			ObjectList<Entry<T>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new CharComparator() {
				@Override
				public int compare(char key, char value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(char entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getCharValue() == entry) return i;
					}
					throw new IllegalArgumentException("Object2CharMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Character> order(List<Character> insertionOrder) {
			ObjectList<Entry<T>> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Character>() {
				@Override
				public int compare(Character key, Character value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Character entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getCharValue() == entry.charValue()) return i;
					}
					throw new IllegalArgumentException("Object2CharMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public CharCollection create(char... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2CharMap.BasicEntry<>(entry.getKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public CharCollection create(Object... elements) {
			Entry<T>[] result = new Entry[elements.length];
			int index = 0;
			for (Entry<T> entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractObject2CharMap.BasicEntry<>(entry.getKey(), (Character)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static<T> Comparator<Map.Entry<T, Character>> entryObjectComparator(Comparator<T> keyComparator) {
		return new Comparator<Map.Entry<T, Character>>() {
			@Override
			public int compare(Map.Entry<T, Character> a, Map.Entry<T, Character> b) {
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
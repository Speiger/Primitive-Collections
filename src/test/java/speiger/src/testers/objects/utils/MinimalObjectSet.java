package speiger.src.testers.objects.utils;

import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;

@SuppressWarnings("javadoc")
public class MinimalObjectSet<T> extends MinimalObjectCollection<T> implements ObjectSet<T> {
	public static <T> MinimalObjectSet<T> of(T...array) {
		return MinimalObjectSet.of(ObjectArrayList.wrap(array));
	}
	
	public static <T> MinimalObjectSet<T> of(ObjectIterable<T> iterable)
	{
		ObjectList<T> list = new ObjectArrayList<>();
		for(ObjectIterator<T> iter = iterable.iterator();iter.hasNext();)
		{
			T key = iter.next();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalObjectSet<>(list.toArray((T[])new Object[list.size()]));
	}
	
	protected MinimalObjectSet(T[] contents) {
		super(contents);
	}
	
	@Override
	public ObjectSet<T> copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public T addOrGet(T o) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ObjectSet) {
			ObjectSet<T> that = (ObjectSet<T>) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (ObjectIterator<T> iter = iterator();iter.hasNext();) {
			hashCodeSum += Objects.hashCode(iter.next());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(Object o) {
		for(ObjectIterator<T> iter = iterator();iter.hasNext();) {
			if(Objects.equals(iter.next(), o)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}
package speiger.src.collections.shorts.functions;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class OptionalShort {
	private static final OptionalShort EMPTY = new OptionalShort();
	
	private final boolean isPresent;
	private final short value;
	
	private OptionalShort() {
		this.isPresent = false;
		this.value = (short)0;
	}
	
	private OptionalShort(short value) {
		this.isPresent = true;
		this.value = value;
	}
	
	public static OptionalShort empty() {
		return EMPTY;
	}
	
	public static OptionalShort of(short value) {
		return new OptionalShort(value);
	}
	
	public short getAsShort() {
		if(!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isEmpty() {
		return !isPresent;
	}
	
	public void ifPresent(ShortConsumer consumer) {
		if(isPresent) consumer.accept(value);
	}
	
    public void ifPresentOrElse(ShortConsumer action, Runnable emptyAction) {
        if (isPresent) action.accept(value);
        else emptyAction.run();
    }
	
	public short orElse(short other) {
		return isPresent ? value : other;
	}
	
	public short orElseGet(ShortSupplier other) {
		return isPresent ? value : other.getAsShort();
	}
	
	public short orElseThrow() {
        if (!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
    public <X extends Throwable> short orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    	if(isPresent) return value;
    	else throw exceptionSupplier.get();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj == this) return true;
    	if(obj instanceof OptionalShort) {
    		OptionalShort other = (OptionalShort)obj;
    		return (isPresent && other.isPresent ? value == other.value : isPresent == other.isPresent);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return isPresent ? Short.hashCode(value) : 0;
    }
    
    @Override
    public String toString() {
    	return isPresent ? "OptionalShort["+value+"]" : "OptionalShort.empty";
    }
}
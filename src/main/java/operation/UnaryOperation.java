package operation;

public interface UnaryOperation<T> extends IGenericOperation<T> {
	T getOperand();
}

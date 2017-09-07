package operation;

public interface IBinaryOperation<T> extends IGenericOperation<T> {

	T getFirstOperand();
	
	T getSecondOperand();
}

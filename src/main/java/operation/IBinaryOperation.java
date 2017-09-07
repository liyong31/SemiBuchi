package operation;

public interface IBinaryOperation<I, O> extends IGenericOperation<I, O> {

	I getFirstOperand();
	
	I getSecondOperand();
}

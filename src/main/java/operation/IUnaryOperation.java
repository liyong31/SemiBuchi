package operation;

public interface IUnaryOperation<I, O> extends IGenericOperation<I, O> {
	I getOperand();
}

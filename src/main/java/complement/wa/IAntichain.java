package complement.wa;

public interface IAntichain<T> {

    boolean covers(T t);
    
    boolean add(T t);
    
}

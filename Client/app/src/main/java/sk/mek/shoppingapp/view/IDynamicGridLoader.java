package sk.mek.shoppingapp.view;

public interface IDynamicGridLoader<T> {

    void display(T[] products, IDynamicGrid iDynamicGrid);

}

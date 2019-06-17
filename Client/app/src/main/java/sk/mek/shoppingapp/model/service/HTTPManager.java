package sk.mek.shoppingapp.model.service;

import java.lang.reflect.Array;
import java.util.HashMap;

import sk.mek.shoppingapp.model.IAsyncResult;

public abstract class HTTPManager<T> {


    Class<T> objectClass;
    Class<T[]> objectArrayClass;

    public HTTPManager(Class<T> objectClass) {

        this.objectClass = objectClass;
        this.objectArrayClass = getArrayClass(objectClass);
    }

    private Class<T[]> getArrayClass(Class<T> clazz) {
        return (Class<T[]>) Array.newInstance(clazz, 0).getClass();
    }


    public void getById(IAsyncResult<T> observer, String urlPath, int id){
        HTTPGetRequestTask<T> getEntityTask = new HTTPGetRequestTask<>(urlPath + "/" + id, objectClass, observer);
        getEntityTask.execute();

    }

    public void getByArgs(IAsyncResult<T[]> observer, String urlHost){

        HTTPGetRequestTask<T[]> getEntityTask = new HTTPGetRequestTask<>(urlHost, objectArrayClass, observer);
        getEntityTask.execute();

    }

    public void getAll(IAsyncResult<T[]> observer, String urlPath) {

        HTTPGetRequestTask<T[]> getAllTask = new HTTPGetRequestTask(urlPath, objectArrayClass, observer);
        getAllTask.execute();

    }
    public void post(IAsyncResult<T> observer, String urlPath, T entity) {

        HTTPPostRequestTask<T> postEntityTask = new HTTPPostRequestTask<>(urlPath, objectClass, observer);
        postEntityTask.execute(buildBodyHashMap(entity));
    }

    public void put(IAsyncResult<T> observer, String urlPath, int id, T entity) {
        HTTPPutRequestTask<T> putEntityTask = new HTTPPutRequestTask<>(urlPath , objectClass, observer);
        putEntityTask.execute(buildBodyHashMap(entity));
    }

    public void delete(IAsyncResult<T> observer, String urlPath, int id) {

        HTTPDeleteRequestTask<T> deleteEntityTask = new HTTPDeleteRequestTask<>(urlPath + "/" + id, objectClass, observer);
        deleteEntityTask.execute();
    }

    public abstract T[] createFake(int num);

    protected abstract HashMap<String, String> buildBodyHashMap(T entity);
}

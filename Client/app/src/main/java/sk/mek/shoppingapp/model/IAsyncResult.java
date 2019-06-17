package sk.mek.shoppingapp.model;

import sk.mek.shoppingapp.model.service.HTTPResult;

public interface IAsyncResult<T> {

    void update(HTTPResult<T> result);
}

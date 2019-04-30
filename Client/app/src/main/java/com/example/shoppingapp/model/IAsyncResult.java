package com.example.shoppingapp.model;

import com.example.shoppingapp.model.service.HTTPResult;

public interface IAsyncResult<T> {

    void update(HTTPResult<T> result);
}

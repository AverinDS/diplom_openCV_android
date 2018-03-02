package com.example.dmitry.diplom_averin.rest;

/**
 * Created by dmitry on 02.03.18.
 */

public class BaseResponse<T> {

    protected T data;

    public T getData() {
        return data;
    }
}

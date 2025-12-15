package com.crediya.domain.response;

import com.crediya.domain.errors.ErrorDomain;

import java.util.function.Consumer;
import java.util.function.Function;

public class ResponseDomain<E extends ErrorDomain, T> {
    private E error;
    private T model;

    public ResponseDomain(E error) {
        this.error = error;
        this.model = null;
    }

    public ResponseDomain(T model) {
        this.error = null;
        this.model = model;
    }

    public boolean hasError() {
        return error != null;
    }


    public E getError() {
        return error;
    }

    public T getModel() {
        return model;
    }

    public <R> R fold(Function<E, R> onError, Function<T, R> onSuccess) {
        if(hasError()) {
            return onError.apply(error);
        } else {
            return onSuccess.apply(model);
        }
    }

    public void when(Consumer<E> onError, Consumer<T> onSuccess) {
        if(hasError()) {
            onError.accept(error);
        } else {
            onSuccess.accept(model);
        }
    }

    public static <E extends ErrorDomain, T> ResponseDomain<E,T> error(E error) {
        return new ResponseDomain(error);
    }

    public static <E extends ErrorDomain, T> ResponseDomain<E,T> success(T model) {
        return new ResponseDomain(model);
    }

    public E getErrorDomain() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }


    public boolean isError() {
        return error != null;
    }
}

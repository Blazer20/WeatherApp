package com.label305.asynctask;

import android.os.Handler;
import java.lang.Exception;
import java.util.concurrent.Executor;

public abstract class AsyncTask<T, E extends Exception> extends SimpleAsyncTask<T> {
    private ExceptionRunnable<E> mOnExceptionRunnable;

    public interface ExceptionRunnable<E extends Throwable> {
        void onException(E e);
    }

    /* access modifiers changed from: protected */
    public abstract T doInBackground() throws Exception;

    protected AsyncTask() {
    }

    protected AsyncTask(Handler handler) {
        super(handler);
    }

    protected AsyncTask(Executor executor) {
        super(executor);
    }

    protected AsyncTask(Handler handler, Executor executor) {
        super(handler, executor);
    }

    public <A extends SimpleAsyncTask<T>> A execute() {
        return execute(new Task(this));
    }

    /* access modifiers changed from: protected */
    public final T doInBackgroundSimple() {
        throw new UnsupportedOperationException("Call doInBackground");
    }

    /* access modifiers changed from: protected */
    public void onException(E e) {
        ExceptionRunnable<E> exceptionRunnable = this.mOnExceptionRunnable;
        if (exceptionRunnable != null) {
            exceptionRunnable.onException(e);
        }
    }

    public AsyncTask<T, E> onException(ExceptionRunnable<E> exceptionRunnable) {
        this.mOnExceptionRunnable = exceptionRunnable;
        return this;
    }
}

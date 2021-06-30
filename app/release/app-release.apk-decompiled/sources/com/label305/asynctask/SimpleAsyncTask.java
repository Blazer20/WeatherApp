package com.label305.asynctask;

import android.os.Handler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public abstract class SimpleAsyncTask<T> {
    private static final String CANCEL_EXCEPTION = "You cannot cancel this task before calling mFutureTask()";
    private static final Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(25);
    private static final int DEFAULT_POOL_SIZE = 25;
    private Executor mExecutor;
    private FutureTask<Void> mFutureTask;
    private Handler mHandler;
    private StackTraceElement[] mLaunchLocation;
    private CancelledRunnable mOnCancelledRunnable;
    private FinallyRunnable mOnFinallyRunnable;
    private InterruptedRunnable mOnInterruptedRunnable;
    private PreExecuteRunnable mOnPreExecuteRunnable;
    private SuccessRunnable<T> mOnSuccessRunnable;

    public interface CancelledRunnable {
        void onCancelled();
    }

    public interface FinallyRunnable {
        void onFinally();
    }

    public interface InterruptedRunnable {
        void onInterrupted(InterruptedException interruptedException);
    }

    public interface PreExecuteRunnable {
        void onPreExecute();
    }

    public interface SuccessRunnable<T> {
        void onSuccess(T t);
    }

    /* access modifiers changed from: protected */
    public abstract T doInBackgroundSimple();

    protected SimpleAsyncTask() {
        this.mExecutor = DEFAULT_EXECUTOR;
    }

    protected SimpleAsyncTask(Handler handler) {
        this.mHandler = handler;
        this.mExecutor = DEFAULT_EXECUTOR;
    }

    protected SimpleAsyncTask(Executor executor) {
        this.mExecutor = executor;
    }

    protected SimpleAsyncTask(Handler handler, Executor executor) {
        this.mHandler = handler;
        this.mExecutor = executor;
    }

    public <A extends SimpleAsyncTask<T>> A execute() {
        return execute(new SimpleTask(this));
    }

    /* access modifiers changed from: package-private */
    public <A extends SimpleAsyncTask<T>> A execute(SimpleTask<T> simpleTask) {
        this.mLaunchLocation = Thread.currentThread().getStackTrace();
        FutureTask<Void> futureTask = new FutureTask<>(simpleTask, (Object) null);
        this.mFutureTask = futureTask;
        this.mExecutor.execute(futureTask);
        return this;
    }

    public boolean cancel() {
        return cancel(false);
    }

    public boolean cancelInterrupt() {
        return cancel(true);
    }

    private boolean cancel(boolean z) {
        FutureTask<Void> futureTask = this.mFutureTask;
        if (futureTask != null) {
            return futureTask.cancel(z);
        }
        throw new UnsupportedOperationException(CANCEL_EXCEPTION);
    }

    public boolean isCancelled() {
        FutureTask<Void> futureTask = this.mFutureTask;
        if (futureTask != null) {
            return futureTask.isCancelled();
        }
        throw new UnsupportedOperationException(CANCEL_EXCEPTION);
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public StackTraceElement[] getLaunchLocation() {
        StackTraceElement[] stackTraceElementArr = this.mLaunchLocation;
        if (stackTraceElementArr == null) {
            return null;
        }
        StackTraceElement[] stackTraceElementArr2 = new StackTraceElement[stackTraceElementArr.length];
        System.arraycopy(stackTraceElementArr, 0, stackTraceElementArr2, 0, stackTraceElementArr.length);
        return stackTraceElementArr2;
    }

    public void setLaunchLocation(StackTraceElement[] stackTraceElementArr) {
        this.mLaunchLocation = stackTraceElementArr;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    public SimpleAsyncTask<T> setExecutor(Executor executor) {
        this.mExecutor = executor;
        return this;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        PreExecuteRunnable preExecuteRunnable = this.mOnPreExecuteRunnable;
        if (preExecuteRunnable != null) {
            preExecuteRunnable.onPreExecute();
        }
    }

    /* access modifiers changed from: protected */
    public void onSuccess(T t) {
        SuccessRunnable<T> successRunnable = this.mOnSuccessRunnable;
        if (successRunnable != null) {
            successRunnable.onSuccess(t);
        }
    }

    /* access modifiers changed from: protected */
    public void onInterrupted(InterruptedException interruptedException) {
        InterruptedRunnable interruptedRunnable = this.mOnInterruptedRunnable;
        if (interruptedRunnable != null) {
            interruptedRunnable.onInterrupted(interruptedException);
            return;
        }
        throw new RuntimeException("Thread was interrupted. Override onInterrupted(InterruptedException) to manage behavior.", interruptedException);
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        CancelledRunnable cancelledRunnable = this.mOnCancelledRunnable;
        if (cancelledRunnable != null) {
            cancelledRunnable.onCancelled();
        }
    }

    /* access modifiers changed from: protected */
    public void onRuntimeException(RuntimeException runtimeException) {
        throw runtimeException;
    }

    /* access modifiers changed from: protected */
    public void onFinally() {
        FinallyRunnable finallyRunnable = this.mOnFinallyRunnable;
        if (finallyRunnable != null) {
            finallyRunnable.onFinally();
        }
    }

    public SimpleAsyncTask<T> onPreExecute(PreExecuteRunnable preExecuteRunnable) {
        this.mOnPreExecuteRunnable = preExecuteRunnable;
        return this;
    }

    public SimpleAsyncTask<T> onSuccess(SuccessRunnable<T> successRunnable) {
        this.mOnSuccessRunnable = successRunnable;
        return this;
    }

    public SimpleAsyncTask<T> onCancelled(CancelledRunnable cancelledRunnable) {
        this.mOnCancelledRunnable = cancelledRunnable;
        return this;
    }

    public SimpleAsyncTask<T> onInterrupted(InterruptedRunnable interruptedRunnable) {
        this.mOnInterruptedRunnable = interruptedRunnable;
        return this;
    }

    public SimpleAsyncTask<T> onFinally(FinallyRunnable finallyRunnable) {
        this.mOnFinallyRunnable = finallyRunnable;
        return this;
    }
}

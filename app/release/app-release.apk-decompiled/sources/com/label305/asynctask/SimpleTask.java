package com.label305.asynctask;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

class SimpleTask<T> implements Runnable {
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public final SimpleAsyncTask<T> mParent;

    SimpleTask(SimpleAsyncTask<T> simpleAsyncTask) {
        this.mParent = simpleAsyncTask;
        this.mHandler = simpleAsyncTask.getHandler() != null ? simpleAsyncTask.getHandler() : new Handler(Looper.getMainLooper());
    }

    public void run() {
        try {
            doPreExecute();
            boolean z = false;
            Object obj = null;
            try {
                obj = doDoInBackgroundSimple();
                z = true;
            } catch (RuntimeException e) {
                doRuntimeException(e);
            }
            if (z) {
                if (this.mParent.isCancelled()) {
                    doCancel();
                } else {
                    doSuccess(obj);
                }
            }
            try {
                doFinally();
            } catch (RuntimeException e2) {
                doRuntimeException(e2);
            }
        } catch (RuntimeException e3) {
            doRuntimeException(e3);
            doFinally();
        } catch (Throwable th) {
            try {
                doFinally();
            } catch (RuntimeException e4) {
                doRuntimeException(e4);
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public final void doPreExecute() {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                SimpleTask.this.mParent.onPreExecute();
            }
        });
    }

    private T doDoInBackgroundSimple() {
        return this.mParent.doInBackgroundSimple();
    }

    /* access modifiers changed from: protected */
    public final void doSuccess(final T t) {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                SimpleTask.this.mParent.onSuccess(t);
            }
        });
    }

    /* access modifiers changed from: protected */
    public final void doCancel() {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                SimpleTask.this.mParent.onCancelled();
            }
        });
    }

    /* access modifiers changed from: protected */
    public final void doRuntimeException(final RuntimeException runtimeException) {
        fixStackTrace(runtimeException);
        this.mHandler.post(new Runnable() {
            public void run() {
                SimpleTask.this.mParent.onRuntimeException(runtimeException);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void fixStackTrace(Exception exc) {
        StackTraceElement[] launchLocation = this.mParent.getLaunchLocation();
        if (launchLocation != null) {
            ArrayList arrayList = new ArrayList(Arrays.asList(exc.getStackTrace()));
            arrayList.addAll(Arrays.asList(launchLocation));
            exc.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[arrayList.size()]));
        }
    }

    /* access modifiers changed from: protected */
    public final void doFinally() {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                SimpleTask.this.mParent.onFinally();
            }
        });
    }

    /* access modifiers changed from: protected */
    public final void postToUiThreadAndWait(final Runnable runnable) {
        final RuntimeException[] runtimeExceptionArr = new RuntimeException[1];
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.mHandler.post(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch (RuntimeException e) {
                    runtimeExceptionArr[0] = e;
                } catch (Throwable th) {
                    countDownLatch.countDown();
                    throw th;
                }
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SimpleTask.this.mParent.onInterrupted(e);
                }
            });
        }
        if (runtimeExceptionArr[0] != null) {
            throw runtimeExceptionArr[0];
        }
    }
}

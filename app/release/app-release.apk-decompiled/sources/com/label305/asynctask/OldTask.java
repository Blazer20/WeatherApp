package com.label305.asynctask;

import android.os.Handler;
import android.os.Looper;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

class OldTask<ResultT, E extends Exception> implements Runnable {
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public final OldAsyncTask<ResultT, E> mParent;

    OldTask(OldAsyncTask<ResultT, E> oldAsyncTask) {
        this.mParent = oldAsyncTask;
        this.mHandler = oldAsyncTask.getHandler() != null ? oldAsyncTask.getHandler() : new Handler(Looper.getMainLooper());
    }

    public void run() {
        try {
            doPreExecute();
            boolean z = false;
            Object obj = null;
            try {
                obj = doDoInBackground();
                z = true;
            } catch (RuntimeException e) {
                doRuntimeException(e);
            } catch (Exception e2) {
                doException(e2);
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
            } catch (RuntimeException e3) {
                doRuntimeException(e3);
            }
        } catch (RuntimeException e4) {
            doRuntimeException(e4);
            doFinally();
        } catch (Throwable th) {
            try {
                doFinally();
            } catch (RuntimeException e5) {
                doRuntimeException(e5);
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void doPreExecute() {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                OldTask.this.mParent.onPreExecute();
            }
        });
    }

    /* access modifiers changed from: protected */
    public ResultT doDoInBackground() throws Exception {
        try {
            return this.mParent.doInBackground();
        } catch (Exception e) {
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public void doSuccess(final ResultT resultt) {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                OldTask.this.mParent.onSuccess(resultt);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void doCancel() {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                OldTask.this.mParent.onCancelled();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void doException(final E e) {
        fixStackTrace(e);
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                OldTask.this.mParent.onException(e);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void doRuntimeException(final RuntimeException runtimeException) {
        fixStackTrace(runtimeException);
        this.mHandler.post(new Runnable() {
            public void run() {
                OldTask.this.mParent.onRuntimeException(runtimeException);
            }
        });
    }

    private void fixStackTrace(Exception exc) {
        StackTraceElement[] launchLocation = this.mParent.getLaunchLocation();
        if (launchLocation != null) {
            ArrayList arrayList = new ArrayList(Arrays.asList(exc.getStackTrace()));
            arrayList.addAll(Arrays.asList(launchLocation));
            exc.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[arrayList.size()]));
        }
    }

    /* access modifiers changed from: protected */
    public void doFinally() {
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                OldTask.this.mParent.onFinally();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void postToUiThreadAndWait(final Runnable runnable) {
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
                    OldTask.this.mParent.onInterrupted(e);
                }
            });
        }
        if (runtimeExceptionArr[0] != null) {
            throw runtimeExceptionArr[0];
        }
    }
}

package com.label305.asynctask;

import java.lang.Exception;

class Task<ResultT, E extends Exception> extends SimpleTask<ResultT> {
    /* access modifiers changed from: private */
    public final AsyncTask<ResultT, E> mParent;

    Task(AsyncTask<ResultT, E> asyncTask) {
        super(asyncTask);
        this.mParent = asyncTask;
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

    private ResultT doDoInBackground() throws Exception {
        try {
            return this.mParent.doInBackground();
        } catch (Exception e) {
            throw e;
        }
    }

    private void doException(final E e) {
        fixStackTrace(e);
        postToUiThreadAndWait(new Runnable() {
            public void run() {
                Task.this.mParent.onException(e);
            }
        });
    }
}

package com.vncreatures.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Intent;

public class MyApplication extends Application {
    // uncaught exception handler variable
    private UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable exception) {

            // here I do logging of exception to a db

            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            System.err.println(stackTrace);

            // re-throw critical exception further to the os (important)
            defaultUEH.uncaughtException(thread, exception);
        }
    };

    public MyApplication() {
        
    }
}

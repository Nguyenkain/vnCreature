package com.vncreatures.customItems;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.vncreatures.controller.KingdomChooseActivity;

import android.content.Context;
import android.content.Intent;

public class MyExceptionHandler implements UncaughtExceptionHandler {

    private final Context myContext;
    private final Class<?> myActivityClass;

    public MyExceptionHandler(Context context, Class<?> c) {

        myContext = context;
        myActivityClass = c;
    }
    
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);// You can use LogCat too
        Intent intent = new Intent(myContext, KingdomChooseActivity.class);
        String s = stackTrace.toString();
        //you can use this String to know what caused the exception and in which Activity
        intent.putExtra("uncaughtException",
                "Exception is: " + stackTrace.toString());
        intent.putExtra("stacktrace", s);
        myContext.startActivity(intent);
        //for restarting the Activity
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}

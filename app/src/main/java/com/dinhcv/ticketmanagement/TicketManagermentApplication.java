package com.dinhcv.ticketmanagement;

import android.app.Application;

import com.android.print.sdk.PrinterInstance;
import com.dinhcv.ticketmanagement.printer.IPrinterOpertion;

import java.net.CookieHandler;
import java.net.CookieManager;


/**
 * Created by dinhcv on 02/04/2017.
 */
public class TicketManagermentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Save cookies of HttpConnection between each time of connection
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

    }

    private PrinterInstance printerInstance;
    public void setIPinter(PrinterInstance printer){
        printerInstance = printer;
    }

    public PrinterInstance getIPrinter(){
        if (printerInstance == null){
            return null;
        }
        return printerInstance;
    }


    private IPrinterOpertion iPrinterOpertion;
    public void setIPrinterOpertion(IPrinterOpertion printer){
        iPrinterOpertion = printer;
    }

    public IPrinterOpertion getIPrinterOpertion(){
        if (iPrinterOpertion == null){
            return null;
        }
        return iPrinterOpertion;
    }




}

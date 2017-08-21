package com.dinhcv.ticketmanagement.printer;

import android.content.Context;
import android.content.res.Resources;

import com.android.print.sdk.Barcode;
import com.android.print.sdk.PrinterConstants.BarcodeType;
import com.android.print.sdk.PrinterConstants.Command;
import com.android.print.sdk.PrinterInstance;
import com.dinhcv.ticketmanagement.R;
import com.dinhcv.ticketmanagement.model.database.ManagerContract;
import com.dinhcv.ticketmanagement.model.structure.Park.Park;
import com.dinhcv.ticketmanagement.model.structure.Statistic.Statistic;
import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;
import com.dinhcv.ticketmanagement.utils.DataUtils;

public class PrintUtils {


	public static void printBill(Context context, Resources resources, PrinterInstance mPrinter, Vehicle vehicle) {
		mPrinter.init();
		mPrinter.setEncoding("UTF-8");
		StringBuffer sb = new StringBuffer();
		// mPrinter.setPrinter(BluetoothPrinter.COMM_LINE_HEIGHT, 80);

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 0);

		Park park = DataUtils.getCurrentPark(context, ManagerContract.ParkEntry.CONTENT_URI);
		String parkName = park.getParkingName();
		String parkAddress = park.getParkingAddress();
		String parkHotline = park.getParkingHotline();
		String parkWebsite = park.getParkingWebsite();

		if (parkName != null) {
			sb.append(parkName + "\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		if (parkAddress != null) {
			sb.append(resources.getString(R.string.add_name)+" "+ parkAddress + "\n");
		}
		if (parkHotline != null) {
			sb.append(resources.getString(R.string.hot_line_name)+" "+ parkHotline + "\n");
		}

		if (parkWebsite != null) {
			sb.append(resources.getString(R.string.web_name)+" "+ parkWebsite + "\n");
		}


		sb.append("==============================\n");
		mPrinter.printText(sb.toString());

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 1);
		mPrinter.printText(resources.getString(R.string.shop_thanks1) +"\n");

		mPrinter.setCharacterMultiple(0, 0);

		// 字号使用默认

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.printText(resources.getString(R.string.time_in) +" "+ vehicle.getTimeIn() +"\n");
		mPrinter.printText(resources.getString(R.string.time_out) +" " + vehicle.getTimeOut() + "\n");
//
		mPrinter.printText(resources.getString(R.string.shop_print_time) +" "+ vehicle.getPrice() + "\n");

		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
	}

	public static void printTicket(Context context, Resources resources, PrinterInstance mPrinter, Vehicle vehicle) {
		mPrinter.init();
		mPrinter.setEncoding("UTF-8");
		StringBuffer sb = new StringBuffer();

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 0);

		Park park = DataUtils.getCurrentPark(context, ManagerContract.ParkEntry.CONTENT_URI);
		String parkName = park.getParkingName();
		String parkAddress = park.getParkingAddress();
		String parkHotline = park.getParkingHotline();
		String parkWebsite = park.getParkingWebsite();

		if (parkName != null) {
			sb.append(parkName + "\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		if (parkAddress != null) {
			sb.append(resources.getString(R.string.add_name)+" "+ parkAddress + "\n");
		}
		if (parkHotline != null) {
			sb.append(resources.getString(R.string.hot_line_name)+" "+ parkHotline + "\n");
		}

		if (parkWebsite != null) {
			sb.append(resources.getString(R.string.web_name)+" "+ parkWebsite + "\n");
		}
		sb.append("==============================\n");

		mPrinter.printText(sb.toString());
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 1);
		mPrinter.printText(resources.getString(R.string.shop_thanks) +"\n");

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);

		String timein = vehicle.getTimeIn();
		mPrinter.printText(resources.getString(R.string.time_in) +" "+ timein +"\n");

//		String licensePlate = vehicle.getLicensePlate();
//		mPrinter.printText(resources.getString(R.string.lisence_plate) + " " + licensePlate + "\n");

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		// Code128
		String license = vehicle.getBarcode();
		Barcode barcode = new Barcode(BarcodeType.CODE128, 2, 80, 2, license);
		mPrinter.printBarCode(barcode);

		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);
	}

	public static void printStatistic(Context context, Resources resources, PrinterInstance mPrinter, Statistic statistic){
		mPrinter.init();
		mPrinter.setEncoding("UTF-8");
		StringBuffer sb = new StringBuffer();

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 0);

		Park park = DataUtils.getCurrentPark(context, ManagerContract.ParkEntry.CONTENT_URI);
		String parkName = park.getParkingName();
		String parkAddress = park.getParkingAddress();
		String parkHotline = park.getParkingHotline();
		String parkWebsite = park.getParkingWebsite();

		if (parkName != null) {
			sb.append(parkName + "\n");
		}
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		if (parkAddress != null) {
			sb.append(resources.getString(R.string.add_name)+" "+ parkAddress + "\n");
		}
		if (parkHotline != null) {
			sb.append(resources.getString(R.string.hot_line_name)+" "+ parkHotline + "\n");
		}

		if (parkWebsite != null) {
			sb.append(resources.getString(R.string.web_name)+" "+ parkWebsite + "\n");
		}
		sb.append("==============================\n");

		mPrinter.printText(sb.toString());
		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_CENTER);
		mPrinter.setCharacterMultiple(0, 1);
		mPrinter.printText(resources.getString(R.string.shop_thanks2) +"\n");

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		// 字号使用默认
		mPrinter.setCharacterMultiple(0, 0);

		mPrinter.setPrinter(Command.ALIGN, Command.ALIGN_LEFT);
		mPrinter.printText(resources.getString(R.string.car_total) +" "+ statistic.getTotalCarInPark() +"\n");

		mPrinter.printText("==============================\n");

		mPrinter.printText(resources.getString(R.string.today) + ":\n");

		mPrinter.printText(resources.getString(R.string.car_in_total) +" "+ statistic.getCarInDay() +"\n");

		mPrinter.printText(resources.getString(R.string.car_out_total) +" "+ statistic.getCarOutDay() +"\n");

		mPrinter.printText(resources.getString(R.string.service_fee) +" "+ statistic.getPriceDay() +"\n");

		mPrinter.printText("==============================\n");

		mPrinter.printText(resources.getString(R.string.statistic_with_time) +":\n");

		mPrinter.printText(resources.getString(R.string.date) +" "+ statistic.getTime1() + " - " + statistic.getTime2() +"\n");

		mPrinter.printText(resources.getString(R.string.carin_total) +" "+ statistic.getTotalCarIn() +"\n");

		mPrinter.printText(resources.getString(R.string.carout_total) +" "+ statistic.getTotalCarOut() +"\n");

		mPrinter.printText(resources.getString(R.string.revenue_total) +" "+ statistic.getTotalPrice() +"\n");

		mPrinter.setPrinter(Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

	}

}

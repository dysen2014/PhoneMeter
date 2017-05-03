package com.dysen.mylibrary.utils.util;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by dy on 2016-09-01.
 */
public class PhoneUtils {

    /**
     * Gps是否可用
     * @param context
     * @return
     */
    public static final boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}

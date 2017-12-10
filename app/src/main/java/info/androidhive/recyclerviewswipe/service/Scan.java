package info.androidhive.recyclerviewswipe.service;

import android.app.Fragment;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by gimba on 15.05.2017.
 */

public class Scan {

    public void scanFromFragment(Fragment fragment) {
        IntentIntegrator integrator =  IntentIntegrator.forFragment(fragment);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }
}

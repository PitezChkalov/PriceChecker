package info.androidhive.recyclerviewswipe.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.MyApplication;
import info.androidhive.recyclerviewswipe.entity.Jewelry;
import timber.log.Timber;

public class UserServiceXML implements FTPDownloadCallback {

    public UserServiceXML(){
        FTPService ftpService = new FTPService();
        ftpService.updateBase(this);
    }

    private List<Jewelry> jewelries;

    private void parseXML() {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            File file = new File(MyApplication.appPath+ "/file_2.xml");
            InputStream fis = new FileInputStream(file);
            parser.setInput(fis, "UTF-8");
        } catch (XmlPullParserException|FileNotFoundException e) {
            Timber.e("parseXML error "+ e.getMessage());
        }


        List<Jewelry> jewelries = new ArrayList<>();
        // продолжаем, пока не достигнем конца документа
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("Entity")) {
                    jewelries.add(new Jewelry(
                            parser.getAttributeValue(19),
                            parser.getAttributeValue(0),
                            parser.getAttributeValue(3),
                            parser.getAttributeValue(3),
                            Integer.parseInt(parser.getAttributeValue(16)),
                            parser.getAttributeValue(15))
                            );
                }
                parser.next();
            }

        }
    catch (Exception e){
        Timber.e("ParseXML "+ e.getMessage());
    }
  this.jewelries = jewelries;
}

public Jewelry findJewelry(String barCode){
        for(Jewelry jewelry:jewelries){
            if(jewelry.getBarCode().equals(barCode))
                return jewelry;
        }
        return null;
}

    @Override
    public void complete() {
     parseXML();
    }
}

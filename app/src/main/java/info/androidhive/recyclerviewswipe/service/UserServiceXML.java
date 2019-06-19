package info.androidhive.recyclerviewswipe.service;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.entity.Jewelry;

public class UserServiceXML {

    public UserServiceXML(Context context){
        parseXML(context);
    }

    private List<Jewelry> jewelries;

    private void parseXML(Context context) {
        XmlPullParser parser = context.getResources().getXml(R.xml.file_2);
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
        Log.e("EXCEPTION", e.getMessage());
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
}

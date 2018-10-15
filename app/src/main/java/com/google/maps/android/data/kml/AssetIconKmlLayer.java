//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.maps.android.data.kml;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.data.Layer;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AssetIconKmlLayer extends Layer {
  public AssetIconKmlLayer(GoogleMap map, int resourceId, Context context)
      throws XmlPullParserException, IOException {
    this(map, context.getResources().openRawResource(resourceId), context);
  }

  public AssetIconKmlLayer(GoogleMap map, InputStream stream, Context context)
      throws XmlPullParserException, IOException {
    if (stream == null) {
      throw new IllegalArgumentException("KML InputStream cannot be null");
    } else {
      AssetIconKmlRenderer mRenderer = new AssetIconKmlRenderer(map, context);
      XmlPullParser xmlPullParser = createXmlParser(stream);
      KmlParser parser = new KmlParser(xmlPullParser);
      parser.parseKml();
      stream.close();
      mRenderer.storeKmlData(parser.getStyles(), parser.getStyleMaps(), parser.getPlacemarks(),
          parser.getContainers(), parser.getGroundOverlays());
      this.storeRenderer(mRenderer);
    }
  }

  private static XmlPullParser createXmlParser(InputStream stream) throws XmlPullParserException {
    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    factory.setNamespaceAware(true);
    XmlPullParser parser = factory.newPullParser();
    parser.setInput(stream, (String) null);
    return parser;
  }

  public void addLayerToMap() throws IOException, XmlPullParserException {
    super.addKMLToMap();
  }

  public boolean hasPlacemarks() {
    return this.hasFeatures();
  }

  public Iterable<KmlPlacemark> getPlacemarks() {
    return (Iterable<KmlPlacemark>) this.getFeatures();
  }

  public boolean hasContainers() {
    return super.hasContainers();
  }

  public Iterable<KmlContainer> getContainers() {
    return super.getContainers();
  }

  public Iterable<KmlGroundOverlay> getGroundOverlays() {
    return super.getGroundOverlays();
  }
}

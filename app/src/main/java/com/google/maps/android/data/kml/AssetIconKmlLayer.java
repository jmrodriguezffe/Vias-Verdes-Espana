//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.maps.android.data.kml;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.Renderer;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AssetIconKmlLayer extends Layer {
  private AssetIconKmlRenderer mRenderer;

  public AssetIconKmlLayer(GoogleMap map, int resourceId, Context context)
      throws XmlPullParserException, IOException {
    this(map, context.getResources().openRawResource(resourceId), context);
  }

  public AssetIconKmlLayer(GoogleMap map, InputStream stream, Context context)
      throws XmlPullParserException, IOException {
    if (stream == null) {
      throw new IllegalArgumentException("KML InputStream cannot be null");
    } else {
      mRenderer = new AssetIconKmlRenderer(map, context);
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

  @Override
  public void setOnFeatureClickListener(final OnFeatureClickListener listener) {
    GoogleMap map = this.getMap();
    map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
      public void onPolygonClick(Polygon polygon) {
        if (AssetIconKmlLayer.this.getFeature(polygon) != null) {
          listener.onFeatureClick(AssetIconKmlLayer.this.getFeature(polygon));
        } else if (AssetIconKmlLayer.this.getContainerFeature(polygon) != null) {
          listener.onFeatureClick(AssetIconKmlLayer.this.getContainerFeature(polygon));
        } else {
          listener.onFeatureClick(findPolygonFeature(polygon));
        }

      }
    });
    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      public boolean onMarkerClick(Marker marker) {
        if (AssetIconKmlLayer.this.getFeature(marker) != null) {
          listener.onFeatureClick(AssetIconKmlLayer.this.getFeature(marker));
        } else if (AssetIconKmlLayer.this.getContainerFeature(marker) != null) {
          listener.onFeatureClick(AssetIconKmlLayer.this.getContainerFeature(marker));
        }

        return false;
      }
    });
    map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
      public void onPolylineClick(Polyline polyline) {
        if (AssetIconKmlLayer.this.getFeature(polyline) != null) {
          listener.onFeatureClick(AssetIconKmlLayer.this.getFeature(polyline));
        } else if (AssetIconKmlLayer.this.getContainerFeature(polyline) != null) {
          listener.onFeatureClick(AssetIconKmlLayer.this.getContainerFeature(polyline));
        }

      }
    });
  }

  private Feature findPolygonFeature(Polygon mapObject) {
    return mRenderer.getPolygonCache().get(mapObject);
  }
}

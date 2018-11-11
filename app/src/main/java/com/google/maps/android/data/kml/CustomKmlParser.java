package com.google.maps.android.data.kml;

import com.google.android.gms.maps.model.GroundOverlay;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CustomKmlParser {
  private static final String STYLE = "Style";
  private static final String STYLE_MAP = "StyleMap";
  private static final String PLACEMARK = "Placemark";
  private static final String GROUND_OVERLAY = "GroundOverlay";
  private static final String CONTAINER_REGEX = "Folder|Document";
  private final XmlPullParser mParser;
  private final HashMap<KmlPlacemark, Object> mPlacemarks;
  private final ArrayList<KmlContainer> mContainers;
  private final HashMap<String, KmlStyle> mStyles;
  private final HashMap<String, String> mStyleMaps;
  private final HashMap<KmlGroundOverlay, GroundOverlay> mGroundOverlays;
  private static final String UNSUPPORTED_REGEX = "altitude|altitudeModeGroup|altitudeMode|begin|bottomFov|cookie|displayName|displayMode|displayMode|end|expires|extrude|flyToView|gridOrigin|httpQuery|leftFov|linkDescription|linkName|linkSnippet|listItemType|maxSnippetLines|maxSessionLength|message|minAltitude|minFadeExtent|minLodPixels|minRefreshPeriod|maxAltitude|maxFadeExtent|maxLodPixels|maxHeight|maxWidth|near|NetworkLink|NetworkLinkControl|overlayXY|range|refreshMode|refreshInterval|refreshVisibility|rightFov|roll|rotationXY|screenXY|shape|sourceHref|state|targetHref|tessellate|tileSize|topFov|viewBoundScale|viewFormat|viewRefreshMode|viewRefreshTime|when";

  public CustomKmlParser(XmlPullParser parser) {
    this.mParser = parser;
    this.mPlacemarks = new HashMap();
    this.mContainers = new ArrayList();
    this.mStyles = new HashMap();
    this.mStyleMaps = new HashMap();
    this.mGroundOverlays = new HashMap();
  }

  public void parseKml() throws XmlPullParserException, IOException {
    for(int eventType = this.mParser.getEventType(); eventType != 1; eventType = this.mParser.next()) {
      if (eventType == 2) {
        if (this.mParser.getName().matches("altitude|altitudeModeGroup|altitudeMode|begin|bottomFov|cookie|displayName|displayMode|displayMode|end|expires|extrude|flyToView|gridOrigin|httpQuery|leftFov|linkDescription|linkName|linkSnippet|listItemType|maxSnippetLines|maxSessionLength|message|minAltitude|minFadeExtent|minLodPixels|minRefreshPeriod|maxAltitude|maxFadeExtent|maxLodPixels|maxHeight|maxWidth|near|NetworkLink|NetworkLinkControl|overlayXY|range|refreshMode|refreshInterval|refreshVisibility|rightFov|roll|rotationXY|screenXY|shape|sourceHref|state|targetHref|tessellate|tileSize|topFov|viewBoundScale|viewFormat|viewRefreshMode|viewRefreshTime|when")) {
          skip(this.mParser);
        }

        if (this.mParser.getName().matches("Folder|Document")) {
          this.mContainers.add(KmlContainerParser.createContainer(this.mParser));
        }

        if (this.mParser.getName().equals("Style")) {
          KmlStyle style = KmlStyleParser.createStyle(this.mParser);
          this.mStyles.put(style.getStyleId(), style);
        }

        if (this.mParser.getName().equals("StyleMap")) {
          this.mStyleMaps.putAll(KmlStyleParser.createStyleMap(this.mParser));
        }

        if (this.mParser.getName().equals("Placemark")) {
          this.mPlacemarks.put(KmlFeatureParser.createPlacemark(this.mParser), null);
        }

        if (this.mParser.getName().equals("GroundOverlay")) {
          this.mGroundOverlays.put(KmlFeatureParser.createGroundOverlay(this.mParser), null);
        }
      }
    }

    this.mStyles.put(null, new KmlStyle());
  }

  HashMap<String, KmlStyle> getStyles() {
    return this.mStyles;
  }

  HashMap<KmlPlacemark, Object> getPlacemarks() {
    return this.mPlacemarks;
  }

  HashMap<String, String> getStyleMaps() {
    return this.mStyleMaps;
  }

  public ArrayList<KmlContainer> getContainers() {
    return this.mContainers;
  }

  HashMap<KmlGroundOverlay, GroundOverlay> getGroundOverlays() {
    return this.mGroundOverlays;
  }

  static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    if (parser.getEventType() != 2) {
      throw new IllegalStateException();
    } else {
      int depth = 1;

      while(depth != 0) {
        switch(parser.next()) {
        case 2:
          ++depth;
          break;
        case 3:
          --depth;
        }
      }

    }
  }
}
package com.google.maps.android.data.kml;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Renderer;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParserException;

public class CustomKmlLayer extends KmlLayer {
  private Renderer mRenderer;

  public CustomKmlLayer(GoogleMap map, int resourceId, Context context)
      throws XmlPullParserException, IOException {
    super(map, resourceId, context);
  }

  public CustomKmlLayer(GoogleMap map, InputStream stream, Context context)
      throws XmlPullParserException, IOException {
    super(map, stream, context);
  }

  @Override
  public void setOnFeatureClickListener(final OnFeatureClickListener listener) {
    GoogleMap map = this.getMap();
    map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
      public void onPolygonClick(Polygon polygon) {
        if (CustomKmlLayer.this.getFeature(polygon) != null) {
          listener.onFeatureClick(CustomKmlLayer.this.getFeature(polygon));
        } else if (CustomKmlLayer.this.getContainerFeature(polygon) != null) {
          listener.onFeatureClick(CustomKmlLayer.this.getContainerFeature(polygon));
        } else {
          listener.onFeatureClick(findPolygonFeature(polygon));
        }

      }
    });
    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      public boolean onMarkerClick(Marker marker) {
        if (CustomKmlLayer.this.getFeature(marker) != null) {
          listener.onFeatureClick(CustomKmlLayer.this.getFeature(marker));
        } else if (CustomKmlLayer.this.getContainerFeature(marker) != null) {
          listener.onFeatureClick(CustomKmlLayer.this.getContainerFeature(marker));
        }

        return false;
      }
    });
    map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
      public void onPolylineClick(Polyline polyline) {
        if (CustomKmlLayer.this.getFeature(polyline) != null) {
          listener.onFeatureClick(CustomKmlLayer.this.getFeature(polyline));
        } else if (CustomKmlLayer.this.getContainerFeature(polyline) != null) {
          listener.onFeatureClick(CustomKmlLayer.this.getContainerFeature(polyline));
        }

      }
    });
  }

  private Feature findPolygonFeature(Polygon mapObject) {

    return null;
  }

  @Override
  protected void storeRenderer(Renderer renderer) {
    this.mRenderer = renderer;
    super.storeRenderer(renderer);
  }
}

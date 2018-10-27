//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.maps.android.data.kml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Geometry;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AssetIconKmlRenderer extends KmlRenderer {
  private static final String LOG_TAG = "KmlRenderer";
  private final ArrayList<String> mGroundOverlayUrls = new ArrayList();
  private final Context mContext;
  private boolean mMarkerIconsDownloaded = false;
  private boolean mGroundOverlayImagesDownloaded = false;
  private HashMap<KmlGroundOverlay, GroundOverlay> mGroundOverlays;
  private ArrayList<KmlContainer> mContainers;

  public Map<Polygon, Feature> getPolygonCache() {
    return mPolygonCache;
  }

  private Map<Polygon, Feature> mPolygonCache = new HashMap<>();

  AssetIconKmlRenderer(GoogleMap map, Context context) {
    super(map, context);
    this.mContext = context;
  }

  private static BitmapDescriptor scaleIcon(Bitmap unscaledBitmap, Double scale) {
    int width = (int) ((double) unscaledBitmap.getWidth() * scale);
    int height = (int) ((double) unscaledBitmap.getHeight() * scale);
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(unscaledBitmap, width, height, false);
    return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
  }

  private void removePlacemarks(HashMap<? extends Feature, Object> placemarks) {
    removeFeatures((HashMap<Feature, Object>) placemarks);
  }

  static boolean getContainerVisibility(KmlContainer kmlContainer,
      boolean isParentContainerVisible) {
    boolean isChildContainerVisible = true;
    if (kmlContainer.hasProperty("visibility")) {
      String placemarkVisibility = kmlContainer.getProperty("visibility");
      if (Integer.parseInt(placemarkVisibility) == 0) {
        isChildContainerVisible = false;
      }
    }

    return isParentContainerVisible && isChildContainerVisible;
  }

  private void removeGroundOverlays(HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {
    Iterator var2 = groundOverlays.values().iterator();

    while (var2.hasNext()) {
      GroundOverlay groundOverlay = (GroundOverlay) var2.next();
      groundOverlay.remove();
    }
  }

  private void removeContainers(Iterable<KmlContainer> containers) {
    Iterator var2 = containers.iterator();

    while (var2.hasNext()) {
      KmlContainer container = (KmlContainer) var2.next();
      this.removePlacemarks(container.getPlacemarksHashMap());
      this.removeGroundOverlays(container.getGroundOverlayHashMap());
      this.removeContainers(container.getContainers());
    }
  }

  public void addLayerToMap() {
    this.setLayerVisibility(true);
    this.mGroundOverlays = this.getGroundOverlayMap();
    this.mContainers = this.getContainerList();
    this.putStyles();
    this.assignStyleMap(this.getStyleMaps(), this.getStylesRenderer());
    this.addGroundOverlays(this.mGroundOverlays, this.mContainers);
    this.addContainerGroupToMap(this.mContainers, true);
    this.addPlacemarksToMap(this.getAllFeatures());
    if (!this.mGroundOverlayImagesDownloaded) {
      this.downloadGroundOverlays();
    }

    if (!this.mMarkerIconsDownloaded) {
      this.downloadMarkerIcons();
    }
  }

  void storeKmlData(HashMap<String, KmlStyle> styles, HashMap<String, String> styleMaps,
      HashMap<KmlPlacemark, Object> features, ArrayList<KmlContainer> folders,
      HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {
    this.storeData(styles, styleMaps, features, folders, groundOverlays);
  }

  public void setMap(GoogleMap map) {
    this.removeLayerFromMap();
    super.setMap(map);
    this.addLayerToMap();
  }

  boolean hasKmlPlacemarks() {
    return this.hasFeatures();
  }

  Iterable<? extends Feature> getKmlPlacemarks() {
    return this.getFeatures();
  }

  public boolean hasNestedContainers() {
    return this.mContainers.size() > 0;
  }

  public Iterable<KmlContainer> getNestedContainers() {
    return this.mContainers;
  }

  public Iterable<KmlGroundOverlay> getGroundOverlays() {
    return this.mGroundOverlays.keySet();
  }

  public void removeLayerFromMap() {
    this.removePlacemarks(this.getAllFeatures());
    this.removeGroundOverlays(this.mGroundOverlays);
    if (this.hasNestedContainers()) {
      this.removeContainers(this.getNestedContainers());
    }

    this.setLayerVisibility(false);
    this.clearStylesRenderer();
  }

  private void addPlacemarksToMap(HashMap<? extends Feature, Object> placemarks) {
    Iterator var2 = placemarks.keySet().iterator();

    while (var2.hasNext()) {
      Feature kmlPlacemark = (Feature) var2.next();
      this.addFeature(kmlPlacemark);
    }
  }

  private void addContainerGroupToMap(Iterable<KmlContainer> kmlContainers,
      boolean containerVisibility) {
    Iterator var3 = kmlContainers.iterator();

    while (var3.hasNext()) {
      KmlContainer container = (KmlContainer) var3.next();
      boolean isContainerVisible = getContainerVisibility(container, containerVisibility);
      if (container.getStyles() != null) {
        this.putStyles(container.getStyles());
      }

      if (container.getStyleMap() != null) {
        super.assignStyleMap(container.getStyleMap(), this.getStylesRenderer());
      }

      this.addContainerObjectToMap(container, isContainerVisible);
      if (container.hasContainers()) {
        this.addContainerGroupToMap(container.getContainers(), isContainerVisible);
      }
    }
  }

  private void addContainerObjectToMap(KmlContainer kmlContainer, boolean isContainerVisible) {
    Iterator var3 = kmlContainer.getPlacemarks().iterator();

    while (var3.hasNext()) {
      Feature placemark = (Feature) var3.next();
      boolean isPlacemarkVisible = getPlacemarkVisibility(placemark);
      boolean isObjectVisible = isContainerVisible && isPlacemarkVisible;
      if (placemark.getGeometry() != null) {
        String placemarkId = placemark.getId();
        Geometry geometry = placemark.getGeometry();
        KmlStyle style = this.getPlacemarkStyle(placemarkId);
        KmlStyle inlineStyle = ((KmlPlacemark) placemark).getInlineStyle();
        Object mapObject =
            this.addKmlPlacemarkToMap((KmlPlacemark) placemark, geometry, style, inlineStyle,
                isObjectVisible);
        kmlContainer.setPlacemark((KmlPlacemark) placemark, mapObject);
        this.putContainerFeature(mapObject, placemark);
        if (mapObject instanceof ArrayList) {
          if (!((ArrayList) mapObject).isEmpty()) {
            for (Object obj : ((ArrayList) mapObject)) {
              if (obj instanceof Polygon) {
                mPolygonCache.put((Polygon) obj, placemark);
              }
            }
          }
        }
      }
    }
  }

  private void downloadMarkerIcons() {
    this.mMarkerIconsDownloaded = true;
    Iterator iterator = this.getMarkerIconUrls().iterator();

    while (iterator.hasNext()) {
      String markerIconUrl = (String) iterator.next();
      (new AssetIconKmlRenderer.MarkerIconImageDownload(markerIconUrl)).execute(new String[0]);
      iterator.remove();
    }
  }

  private void addIconToMarkers(String iconUrl, HashMap<KmlPlacemark, Object> placemarks) {
    Iterator var3 = placemarks.keySet().iterator();

    while (true) {
      Feature placemark;
      KmlStyle urlStyle;
      KmlStyle inlineStyle;
      do {
        if (!var3.hasNext()) {
          return;
        }

        placemark = (Feature) var3.next();
        urlStyle = (KmlStyle) this.getStylesRenderer().get(placemark.getId());
        inlineStyle = ((KmlPlacemark) placemark).getInlineStyle();
      } while (!"Point".equals(placemark.getGeometry().getGeometryType()));

      boolean isInlineStyleIcon = inlineStyle != null && iconUrl.equals(inlineStyle.getIconUrl());
      boolean isPlacemarkStyleIcon = urlStyle != null && iconUrl.equals(urlStyle.getIconUrl());
      if (isInlineStyleIcon) {
        this.scaleBitmap(inlineStyle, placemarks, (KmlPlacemark) placemark);
      } else if (isPlacemarkStyleIcon) {
        this.scaleBitmap(urlStyle, placemarks, (KmlPlacemark) placemark);
      }
    }
  }

  private void scaleBitmap(KmlStyle style, HashMap<KmlPlacemark, Object> placemarks,
      KmlPlacemark placemark) {
    double bitmapScale = style.getIconScale();
    String bitmapUrl = style.getIconUrl();
    Bitmap bitmapImage = (Bitmap) this.getImagesCache().get(bitmapUrl);
    BitmapDescriptor scaledBitmap = scaleIcon(bitmapImage, bitmapScale);
    ((Marker) placemarks.get(placemark)).setIcon(scaledBitmap);
  }

  private void addContainerGroupIconsToMarkers(String iconUrl,
      Iterable<KmlContainer> kmlContainers) {
    Iterator var3 = kmlContainers.iterator();

    while (var3.hasNext()) {
      KmlContainer container = (KmlContainer) var3.next();
      this.addIconToMarkers(iconUrl, container.getPlacemarksHashMap());
      if (container.hasContainers()) {
        this.addContainerGroupIconsToMarkers(iconUrl, container.getContainers());
      }
    }
  }

  private void addGroundOverlays(HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays,
      Iterable<KmlContainer> kmlContainers) {
    this.addGroundOverlays(groundOverlays);
    Iterator var3 = kmlContainers.iterator();

    while (var3.hasNext()) {
      KmlContainer container = (KmlContainer) var3.next();
      this.addGroundOverlays(container.getGroundOverlayHashMap(), container.getContainers());
    }
  }

  private void addGroundOverlays(HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays) {
    Iterator var2 = groundOverlays.keySet().iterator();

    while (var2.hasNext()) {
      KmlGroundOverlay groundOverlay = (KmlGroundOverlay) var2.next();
      String groundOverlayUrl = groundOverlay.getImageUrl();
      if (groundOverlayUrl != null && groundOverlay.getLatLngBox() != null) {
        if (this.getImagesCache().get(groundOverlayUrl) != null) {
          this.addGroundOverlayToMap(groundOverlayUrl, this.mGroundOverlays, true);
        } else if (!this.mGroundOverlayUrls.contains(groundOverlayUrl)) {
          this.mGroundOverlayUrls.add(groundOverlayUrl);
        }
      }
    }
  }

  private void downloadGroundOverlays() {
    this.mGroundOverlayImagesDownloaded = true;
    Iterator iterator = this.mGroundOverlayUrls.iterator();

    while (iterator.hasNext()) {
      String groundOverlayUrl = (String) iterator.next();
      (new AssetIconKmlRenderer.GroundOverlayImageDownload(groundOverlayUrl)).execute(
          new String[0]);
      iterator.remove();
    }
  }

  private void addGroundOverlayToMap(String groundOverlayUrl,
      HashMap<KmlGroundOverlay, GroundOverlay> groundOverlays, boolean containerVisibility) {
    BitmapDescriptor groundOverlayBitmap =
        BitmapDescriptorFactory.fromBitmap((Bitmap) this.getImagesCache().get(groundOverlayUrl));
    Iterator var5 = groundOverlays.keySet().iterator();

    while (var5.hasNext()) {
      KmlGroundOverlay kmlGroundOverlay = (KmlGroundOverlay) var5.next();
      if (kmlGroundOverlay.getImageUrl().equals(groundOverlayUrl)) {
        GroundOverlayOptions groundOverlayOptions =
            kmlGroundOverlay.getGroundOverlayOptions().image(groundOverlayBitmap);
        GroundOverlay mapGroundOverlay = this.attachGroundOverlay(groundOverlayOptions);
        if (!containerVisibility) {
          mapGroundOverlay.setVisible(false);
        }

        groundOverlays.put(kmlGroundOverlay, mapGroundOverlay);
      }
    }
  }

  private void addGroundOverlayInContainerGroups(String groundOverlayUrl,
      Iterable<KmlContainer> kmlContainers, boolean containerVisibility) {
    Iterator var4 = kmlContainers.iterator();

    while (var4.hasNext()) {
      KmlContainer container = (KmlContainer) var4.next();
      boolean isContainerVisible = getContainerVisibility(container, containerVisibility);
      this.addGroundOverlayToMap(groundOverlayUrl, container.getGroundOverlayHashMap(),
          isContainerVisible);
      if (container.hasContainers()) {
        this.addGroundOverlayInContainerGroups(groundOverlayUrl, container.getContainers(),
            isContainerVisible);
      }
    }
  }

  private class GroundOverlayImageDownload extends AsyncTask<String, Void, Bitmap> {
    private final String mGroundOverlayUrl;

    public GroundOverlayImageDownload(String groundOverlayUrl) {
      this.mGroundOverlayUrl = groundOverlayUrl;
    }

    protected Bitmap doInBackground(String... params) {
      try {
        return BitmapFactory.decodeStream(
            (InputStream) (new URL(this.mGroundOverlayUrl)).getContent());
      } catch (MalformedURLException var3) {
        return BitmapFactory.decodeFile(this.mGroundOverlayUrl);
      } catch (IOException var4) {
        Log.e("KmlRenderer", "Image [" + this.mGroundOverlayUrl + "] download issue", var4);
        return null;
      }
    }

    protected void onPostExecute(Bitmap bitmap) {
      if (bitmap == null) {
        Log.e("KmlRenderer", "Image at this URL could not be found " + this.mGroundOverlayUrl);
      } else {
        AssetIconKmlRenderer.this.putImagesCache(this.mGroundOverlayUrl, bitmap);
        if (AssetIconKmlRenderer.this.isLayerOnMap()) {
          AssetIconKmlRenderer.this.addGroundOverlayToMap(this.mGroundOverlayUrl,
              AssetIconKmlRenderer.this.mGroundOverlays, true);
          AssetIconKmlRenderer.this.addGroundOverlayInContainerGroups(this.mGroundOverlayUrl,
              AssetIconKmlRenderer.this.mContainers, true);
        }
      }
    }
  }

  private class MarkerIconImageDownload extends AsyncTask<String, Void, Bitmap> {
    private final String mIconUrl;

    public MarkerIconImageDownload(String iconUrl) {
      this.mIconUrl = iconUrl;
    }

    protected Bitmap doInBackground(String... params) {
      try {
        return BitmapFactory.decodeStream(mContext.getAssets().open(this.mIconUrl));
      } catch (MalformedURLException var3) {
        return BitmapFactory.decodeFile(this.mIconUrl);
      } catch (IOException var4) {
        var4.printStackTrace();
        return null;
      }
    }

    protected void onPostExecute(Bitmap bitmap) {
      if (bitmap == null) {
        Log.e("KmlRenderer", "Image at this URL could not be found " + this.mIconUrl);
      } else {
        AssetIconKmlRenderer.this.putImagesCache(this.mIconUrl, bitmap);
        if (AssetIconKmlRenderer.this.isLayerOnMap()) {
          AssetIconKmlRenderer.this.addIconToMarkers(this.mIconUrl,
              (HashMap<KmlPlacemark, Object>) AssetIconKmlRenderer.this.getAllFeatures());
          AssetIconKmlRenderer.this.addContainerGroupIconsToMarkers(this.mIconUrl,
              AssetIconKmlRenderer.this.mContainers);
        }
      }
    }
  }
}

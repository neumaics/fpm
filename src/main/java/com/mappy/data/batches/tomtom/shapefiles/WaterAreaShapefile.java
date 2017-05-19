package com.mappy.data.batches.tomtom.shapefiles;

import com.mappy.data.batches.tomtom.TomtomFolder;
import com.mappy.data.batches.tomtom.TomtomShapefile;
import com.mappy.data.batches.tomtom.dbf.names.NameProvider;
import com.mappy.data.batches.utils.Feature;
import com.mappy.data.batches.utils.GeometrySerializer;
import com.mappy.data.batches.utils.LargePolygonSplitter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import javax.inject.Inject;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class WaterAreaShapefile extends TomtomShapefile {

    private final NameProvider nameProvider;

    @Inject
    public WaterAreaShapefile(NameProvider nameProvider, TomtomFolder folder) {
        super(folder.getFile("wa.shp"));
        this.nameProvider = nameProvider;
        this.nameProvider.loadFromFile("wxnm.dbf", "NAME", false);
    }

    public void serialize(GeometrySerializer serializer, Feature feature) {
        Integer type = feature.getInteger("TYP");
        Map<String, String> tags = newHashMap();
        if (!type.equals(1)) {
            String name = feature.getString("NAME");
            if (name != null) {
                tags.put("name", name);
            }
            tags.putAll(nameProvider.getAlternateNames(feature.getLong("ID")));
            if (type.equals(2)) {
                tags.put("water", "lake");
            }
        }
        tags.put("natural", "water");
        for (Geometry geometry : LargePolygonSplitter.split(feature.getMultiPolygon(), 0.01)) {
            write(serializer, tags, geometry);
        }
    }

    private static void write(GeometrySerializer serializer, Map<String, String> tags, Geometry intersection) {
        if (intersection instanceof Polygon) {
            serializer.write((Polygon) intersection, tags);
        }
        else if (intersection instanceof MultiPolygon) {
            serializer.write((MultiPolygon) intersection, tags);
        }
        else {
            throw new RuntimeException("Wrong type");
        }
    }

}

package com.mappy.data.batches.naturalearth.shapefiles;

import com.mappy.data.utils.MemoryGeometrySerializer;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class AirportsShapefileTest {
    private final MemoryGeometrySerializer serializer = new MemoryGeometrySerializer();
    private final AirportsShapefile shp = new AirportsShapefile(getClass().getResource("/naturalearth").getPath());

    @Test
    public void should_serialize_airports() throws Exception {
        shp.serialize(serializer);

        assertThat(serializer.getPoints())
                .extracting(m -> m.get("name"))
                .containsOnly("Charles de Gaulle Int'l", "Paris Orly");

        assertThat(serializer.getPoints().get(0))
                .containsEntry("name", "Charles de Gaulle Int'l")
                .containsEntry("aeroway", "aerodrome");
    }
}
package com.mappy.fpm.batches.tomtom.shapefiles;

import com.mappy.fpm.batches.tomtom.Tomtom2Osm;
import com.mappy.fpm.batches.tomtom.Tomtom2OsmModule;
import com.mappy.fpm.batches.tomtom.Tomtom2OsmTestUtils.PbfContent;
import net.morbz.osmonaut.osm.Tags;
import net.morbz.osmonaut.osm.Way;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

import static com.google.inject.Guice.createInjector;
import static com.mappy.fpm.batches.tomtom.Tomtom2OsmTestUtils.read;
import static org.assertj.core.api.Assertions.assertThat;

public class RoadShapefileTest {

    @Test
    public void should_change_name_to_french_when_available() throws Exception {
        Tomtom2Osm launcher = createInjector(new Tomtom2OsmModule("src/test/resources/osmgenerator/", "target", "target", "multiSidesNames")).getInstance(Tomtom2Osm.class);
        launcher.run();
        PbfContent pbfContent = read(new File("target/multiSidesNames.osm.pbf"));

        Optional<Way> wayOptional = pbfContent.getWays().stream().filter(way -> way.getTags().hasKeyValue("ref:tomtom", "12500001097987")).findFirst();
        assertThat(wayOptional.isPresent()).isTrue();
        Tags tags = wayOptional.get().getTags();
        assertThat(tags.get("name")).isEqualTo("Boulevard Exelmans");
        assertThat(tags.get("name:left")).isEqualTo("Boulevard Exelmans");
        assertThat(tags.get("name:left:fr")).isEqualTo("Boulevard Exelmans");
        assertThat(tags.get("name:right")).isEqualTo("Place Claude François");
        assertThat(tags.get("name:right:fr")).isEqualTo("Place Claude François");
        assertThat(tags.get("from:tomtom")).isEqualTo("12500003459999");
        assertThat(tags.get("to:tomtom")).isEqualTo("12500003122934");
        assertThat(tags.get("reversed:tomtom")).isEqualTo("yes");

        assertThat(tags.get("toll")).isEqualTo("yes");
        assertThat(tags.get("toll:name")).isEqualTo("Péage d'Aigrefeuille");
        assertThat(tags.get("ref:asfa:1")).isEqualTo("4556");
        assertThat(tags.get("ref:asfa:2")).isEqualTo("4557");
    }
}

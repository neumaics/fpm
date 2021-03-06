package com.mappy.fpm.batches.tomtom.dbf.lanes;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.mappy.fpm.batches.utils.Feature;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.reverse;
import static com.mappy.fpm.batches.tomtom.helpers.RoadTagger.isReversed;

public class LaneTagger {
    private final LaneDirections directions;

    @Inject
    public LaneTagger(LaneDirections directions) {
        this.directions = directions;
    }

    public Map<String, String> lanesFor(Feature feature) {
        Map<String, String> tags = Maps.newHashMap();
        if (directions.containsKey(feature.getLong("ID"))) {
            tags.put("turn:lanes", text(feature));
        }
        Integer lanes = feature.getInteger("LANES");
        if (lanes > 0) {
            tags.put("lanes", String.valueOf(lanes));
        }
        return tags;
    }

    private String text(Feature feature) {
        return Joiner.on("|").join(reorder(feature));
    }

    private List<String> reorder(Feature feature) {
        List<String> parts = directions.get(feature.getLong("ID"));
        if (isReversed(feature)) {
            return parts;
        }
        return reverse(parts);
    }

}

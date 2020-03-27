package common.layer;

import static common.model.region.RegionType.BASE_1;
import static common.model.region.RegionType.BASE_2;
import static common.model.region.RegionType.BASE_3;
import static common.model.region.RegionType.BASE_4;
import static common.model.region.RegionType.LAND;
import static common.model.region.RegionType.MINE;
import static common.model.region.RegionType.WALL;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;

import common.model.region.RegionType;

public class LayoutMapping {

    public static final String ROW_DELIMITER = "\r\n";
    public static final String COLUMN_DELIMITER = ",";

    private static final BiMap<RegionType, Character> biDirectionalMapping = HashBiMap.create(
            ImmutableMap.<RegionType, Character>builder()
                    .put(LAND, ' ')
                    .put(WALL, '#')
                    .put(MINE, '$')
                    .put(BASE_1, '1')
                    .put(BASE_2, '2')
                    .put(BASE_3, '3')
                    .put(BASE_4, '4')
                    .build());

    public static RegionType symbolToRegionType(Character symbol) {
        return biDirectionalMapping.inverse().get(symbol);
    }

    public static Character regionTypeToSymbol(RegionType regionType) {
        return biDirectionalMapping.get(regionType);
    }
}

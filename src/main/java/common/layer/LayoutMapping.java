package common.layer;

import static common.model.game.RegionType.BASE;
import static common.model.game.RegionType.LAND;
import static common.model.game.RegionType.MINE;
import static common.model.game.RegionType.WALL;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;

import common.model.game.RegionType;

public class LayoutMapping {

    public static final String ROW_DELIMITER = "\r\n";
    public static final String COLUMN_DELIMITER = ",";

    private static final BiMap<RegionType, Character> biDirectionalMapping = HashBiMap.create(
            ImmutableMap.<RegionType, Character>builder()
                    .put(LAND, ' ')
                    .put(WALL, '#')
                    .put(MINE, '$')
                    .put(BASE, '@')
                    .build());

    public static RegionType symbolToRegionType(Character symbol) {
        return biDirectionalMapping.inverse().get(symbol);
    }

    public static Character regionTypeToSymbol(RegionType regionType) {
        return biDirectionalMapping.get(regionType);
    }
}

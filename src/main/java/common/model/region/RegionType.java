package common.model.region;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum RegionType {
    WALL, LAND, MINE, BASE_1, BASE_2, BASE_3, BASE_4;

    public static final List<RegionType> BASES = ImmutableList.of(BASE_1, BASE_2, BASE_3, BASE_4);
}

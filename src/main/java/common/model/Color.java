package common.model;

import static common.model.RegionType.BASE_1;
import static common.model.RegionType.BASE_2;
import static common.model.RegionType.BASE_3;
import static common.model.RegionType.BASE_4;
import static java.util.Arrays.stream;

public enum Color {
    RED(BASE_1), GREEN(BASE_2), BLUE(BASE_3), PURPLE(BASE_4);

    private RegionType regionType;

    Color(RegionType regionType) {
        this.regionType = regionType;
    }

    public static Color getByRegionType(RegionType regionType) {
        return stream(values()).filter(color -> color.regionType == regionType).findFirst().orElse(null);
    }
}

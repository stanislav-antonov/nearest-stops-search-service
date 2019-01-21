package pse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

final class Stop implements Dimensional {

    @Expose
    @SerializedName("st_id")
    private final long mId;

    @Expose
    @SerializedName("st_title")
    private final String mTitle;

    @Expose
    @SerializedName("st_title_en")
    private final String mTitleEn;

    @Expose
    @SerializedName("st_lat")
    private final double mLat;

    @Expose
    @SerializedName("st_long")
    private final double mLng;

    Stop(long id, String title, double lat, double lng) {
        mId = id;
        mTitle = title;
        mLat = lat;
        mLng = lng;

        mTitleEn = "";
    }

    @Override
    public Double[] getCoords() {
        return new Double[] { mLat, mLng };
    }
}

package pse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

final class Stop implements Dimensional {

    @Expose
    @SerializedName("id")
    private final long mId;

    @Expose
    @SerializedName("name")
    private final String mName;

    @Expose
    @SerializedName("lat")
    private final double mLat;

    @Expose
    @SerializedName("lng")
    private final double mLng;

    Stop(long id, String name, double lat, double lng) {
        mId = id;
        mName = name;
        mLat = lat;
        mLng = lng;
    }

    @Override
    public Double[] getCoords() {
        return new Double[] { mLat, mLng };
    }
}

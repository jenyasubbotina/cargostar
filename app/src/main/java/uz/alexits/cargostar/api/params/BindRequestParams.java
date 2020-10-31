package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class BindRequestParams {
    @SerializedName("employee_id")
    private final long courierId;

    public BindRequestParams(final long courierId) {
        this.courierId = courierId;
    }

    public long getCourierId() {
        return courierId;
    }

    @NonNull
    @Override
    public String toString() {
        return "BindRequestParams{" +
                ", courierId=" + courierId +
                '}';
    }
}

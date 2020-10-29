package uz.alexits.cargostar.api.params;

import com.google.gson.annotations.SerializedName;

public class BindRequestParams {
    @SerializedName("request_id")
    private final long requestId;

    @SerializedName("employee_id")
    private final long courierId;

    public BindRequestParams(final long requestId, final long courierId) {
        this.requestId = requestId;
        this.courierId = courierId;
    }

    public long getRequestId() {
        return requestId;
    }

    public long getCourierId() {
        return courierId;
    }

    @Override
    public String toString() {
        return "BindRequestParams{" +
                "requestId=" + requestId +
                ", courierId=" + courierId +
                '}';
    }
}

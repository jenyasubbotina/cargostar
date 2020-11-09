package uz.alexits.cargostar.model.transportation;

import java.util.List;

public class StatusArrayTransitPoint {
    private Long transitPoint;
    private List<Long> statusArray;

    public StatusArrayTransitPoint() {
        this(null, null);
    }

    public StatusArrayTransitPoint(final List<Long> statusArray, final Long transitPoint) {
        this.statusArray = statusArray;
        this.transitPoint = transitPoint;
    }

    public void setStatusArray(List<Long> statusArray) {
        this.statusArray = statusArray;
    }

    public void setTransitPoint(Long transitPoint) {
        this.transitPoint = transitPoint;
    }

    public List<Long> getStatusArray() {
        return statusArray;
    }

    public Long getTransitPoint() {
        return transitPoint;
    }
}

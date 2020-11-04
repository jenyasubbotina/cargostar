package uz.alexits.cargostar.model.calculation;

import java.util.List;

public class TypePackageIdList {
    private Integer type;
    private List<Long> packagingIdList;

    public TypePackageIdList() {
        this(null, null);
    }

    public TypePackageIdList(final Integer type, final List<Long> packagingIdList) {
        this.type = type;
        this.packagingIdList = packagingIdList;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setPackagingIdList(List<Long> packagingIdList) {
        this.packagingIdList = packagingIdList;
    }

    public Integer getType() {
        return type;
    }

    public List<Long> getPackagingIdList() {
        return packagingIdList;
    }
}

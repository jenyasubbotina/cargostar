//package com.example.cargostar.model.shipping;
//
//import androidx.annotation.NonNull;
//import androidx.room.Embedded;
//import androidx.room.Relation;
//
//import java.util.List;
//
//public class ConsolidationWithParcelList {
//    @Embedded private Consolidation consolidation;
//    @Relation(parentColumn = "consolidation_number", entityColumn = "consolidation_number") private List<Parcel> parcelList;
//
//    public Consolidation getConsolidation() {
//        return consolidation;
//    }
//
//    public void setConsolidation(Consolidation consolidation) {
//        this.consolidation = consolidation;
//    }
//
//    public List<Parcel> getParcelList() {
//        return parcelList;
//    }
//
//    public void setParcelList(List<Parcel> parcelList) {
//        this.parcelList = parcelList;
//    }
//
//    @NonNull
//    @Override
//    public String toString() {
//        return "ConsolidationWithParcelList{" +
//                "consolidation=" + consolidation +
//                ", parcelList=" + parcelList +
//                '}';
//    }
//}

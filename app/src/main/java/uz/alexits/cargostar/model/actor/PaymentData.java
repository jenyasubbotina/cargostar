//package com.example.cargostar.model.actor;
//
//import androidx.annotation.NonNull;
//import androidx.room.ColumnInfo;
//import androidx.room.Embedded;
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.Index;
//import androidx.room.PrimaryKey;
//
//import com.example.cargostar.model.Document;
//
//@Entity(tableName = "payment_data",
//        inheritSuperIndices = true,
//        foreignKeys = {@ForeignKey(entity = Customer.class,
//        parentColumns = "id",
//        childColumns = "user_id",
//        onDelete = ForeignKey.CASCADE,
//        onUpdate = ForeignKey.CASCADE)},
//        indices = {@Index(value = {"user_id"}, unique = true)})
//public class PaymentData {
//    @PrimaryKey
//    @ColumnInfo(name = "user_id") private long userId;
//    @ColumnInfo(name = "inn") @NonNull private String inn;
//    @ColumnInfo(name = "mfo") private String mfo;
//    @ColumnInfo(name = "oked") private String oked;
//    @ColumnInfo(name = "company") @NonNull private String company;
//    @ColumnInfo(name = "bank") private String bank;
//    @ColumnInfo(name = "checking_account") private String checkingAccount;
//    @ColumnInfo(name = "vat") private String vat;
//    @Embedded(prefix = "contract_") private Document contract;
//
//    public PaymentData(@NonNull final String inn, @NonNull final String company) {
//        this.inn = inn;
//        this.company = company;
//    }
//
//    public void setUserId(long userId) {
//        this.userId = userId;
//    }
//
//    public long getUserId() {
//        return userId;
//    }
//
//    @NonNull
//    public String getInn() {
//        return inn;
//    }
//
//    public void setInn(@NonNull final  String inn) {
//        this.inn = inn;
//    }
//
//    public String getMfo() {
//        return mfo;
//    }
//
//    public void setMfo(String mfo) {
//        this.mfo = mfo;
//    }
//
//    public String getOked() {
//        return oked;
//    }
//
//    public void setOked(String oked) {
//        this.oked = oked;
//    }
//
//    @NonNull
//    public String getCompany() {
//        return company;
//    }
//
//    public void setCompany(@NonNull final String company) {
//        this.company = company;
//    }
//
//    public String getBank() {
//        return bank;
//    }
//
//    public void setBank(String bank) {
//        this.bank = bank;
//    }
//
//    public String getCheckingAccount() {
//        return checkingAccount;
//    }
//
//    public void setCheckingAccount(String checkingAccount) {
//        this.checkingAccount = checkingAccount;
//    }
//
//    public String getVat() {
//        return vat;
//    }
//
//    public void setVat(String vat) {
//        this.vat = vat;
//    }
//
//    public Document getContract() {
//        return contract;
//    }
//
//    public void setContract(Document contract) {
//        this.contract = contract;
//    }
//
//    @NonNull
//    @Override
//    public String toString() {
//        return "PaymentData{" +
//                "userId='" + userId + '\'' +
//                ", inn='" + inn + '\'' +
//                ", mfo='" + mfo + '\'' +
//                ", oked='" + oked + '\'' +
//                ", company='" + company + '\'' +
//                ", bank='" + bank + '\'' +
//                ", checkingAccount='" + checkingAccount + '\'' +
//                ", vat='" + vat + '\'' +
//                ", contract=" + contract +
//                '}';
//    }
//}
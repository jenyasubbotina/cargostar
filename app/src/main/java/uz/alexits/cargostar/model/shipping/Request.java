package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.packaging.Provider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "request",
        foreignKeys = {
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Region.class, parentColumns = "id", childColumns = "region_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "id", childColumns = "city_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "country_id"), @Index(value = "region_id"), @Index(value = "city_id"), @Index(value = "provider_id")})
public class Request {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id")
    private long senderCountryId;

    @Expose
    @SerializedName("region_id")
    @ColumnInfo(name = "region_id")
    private long senderRegionId;

    @Expose
    @SerializedName("city_id")
    @ColumnInfo(name = "city_id")
    private long senderCityId;

    @Expose
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private long userId;

    @Expose
    @SerializedName("client_id")
    @ColumnInfo(name = "client_id")
    private long clientId;

    @Expose
    @SerializedName("employee_id")
    @ColumnInfo(name = "employee_id")
    private long courierId;

    @Expose
    @SerializedName("provider_id")
    @ColumnInfo(name = "provider_id")
    private long providerId;

    @Expose
    @SerializedName("invoice_id")
    @ColumnInfo(name = "invoice_id")
    private long invoiceId;

    @Expose
    @SerializedName("firstname")
    @ColumnInfo(name = "first_name")
    private String senderFirstName;

    @Expose
    @SerializedName("middlename")
    @ColumnInfo(name = "middle_name")
    private String senderMiddleName;

    @Expose
    @SerializedName("lastname")
    @ColumnInfo(name = "last_name")
    private String senderLastName;

    @Expose
    @SerializedName("email")
    @ColumnInfo(name = "email")
    private String senderEmail;

    @Expose
    @SerializedName("telephone")
    @ColumnInfo(name = "phone")
    private String senderPhone;

    @Expose
    @SerializedName("adres")
    @ColumnInfo(name = "address")
    private String senderAddress;

    @Expose
    @SerializedName("country_to")
    @ColumnInfo(name = "country_to")
    private long recipientCountryId;

    @Expose
    @SerializedName("city_to")
    @ColumnInfo(name = "city_to")
    private long recipientCityId;

    @Expose
    @SerializedName("comment")
    @ColumnInfo(name = "comment")
    private String comment;

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    private int status;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @Expose
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public Request(final long id,
                   final String senderFirstName,
                   final String senderMiddleName,
                   final String senderLastName,
                   final String senderEmail,
                   final String senderPhone,
                   final String senderAddress,
                   final long senderCountryId,
                   final long senderRegionId,
                   final long senderCityId,
                   final long recipientCountryId,
                   final long recipientCityId,
                   final String comment,
                   final long userId,
                   final long clientId,
                   final long courierId,
                   final long providerId,
                   final long invoiceId,
                   final int status,
                   final Date createdAt,
                   final Date updatedAt) {
        this.id = id;
        this.senderFirstName = senderFirstName;
        this.senderMiddleName = senderMiddleName;
        this.senderLastName = senderLastName;
        this.senderEmail = senderEmail;
        this.senderPhone = senderPhone;
        this.senderAddress = senderAddress;
        this.senderCountryId = senderCountryId;
        this.senderRegionId = senderRegionId;
        this.senderCityId = senderCityId;
        this.recipientCountryId = recipientCountryId;
        this.recipientCityId = recipientCityId;
        this.comment = comment;
        this.userId = userId;
        this.clientId = clientId;
        this.courierId = courierId;
        this.providerId = providerId;
        this.invoiceId = invoiceId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderMiddleName() {
        return senderMiddleName;
    }

    public void setSenderMiddleName(String senderMiddleName) {
        this.senderMiddleName = senderMiddleName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public long getSenderCountryId() {
        return senderCountryId;
    }

    public void setSenderCountryId(long senderCountryId) {
        this.senderCountryId = senderCountryId;
    }

    public long getSenderRegionId() {
        return senderRegionId;
    }

    public void setSenderRegionId(long senderRegionId) {
        this.senderRegionId = senderRegionId;
    }

    public long getSenderCityId() {
        return senderCityId;
    }

    public void setSenderCityId(long senderCityId) {
        this.senderCityId = senderCityId;
    }

    public long getRecipientCountryId() {
        return recipientCountryId;
    }

    public void setRecipientCountryId(final long recipientCountryId) {
        this.recipientCountryId = recipientCountryId;
    }

    public long getRecipientCityId() {
        return recipientCityId;
    }

    public void setRecipientCityId(final long recipientCityId) {
        this.recipientCityId = recipientCityId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", senderCountryId=" + senderCountryId +
                ", senderRegionId=" + senderRegionId +
                ", senderCityId=" + senderCityId +
                ", userId=" + userId +
                ", clientId=" + clientId +
                ", courierId=" + courierId +
                ", providerId=" + providerId +
                ", invoiceId=" + invoiceId +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderMiddleName='" + senderMiddleName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", senderPhone='" + senderPhone + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", recipientCountryId='" + recipientCountryId + '\'' +
                ", recipientCityId='" + recipientCityId + '\'' +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

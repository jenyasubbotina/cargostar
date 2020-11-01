package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.calculation.Provider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

//todo: foreign keys: invoiceId, clientId, userId
@Entity(tableName = "request",
        foreignKeys = {
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "sender_country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Region.class, parentColumns = "id", childColumns = "sender_region_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "id", childColumns = "sender_city_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "recipient_country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "id", childColumns = "recipient_city_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "sender_country_id"),
                @Index(value = "sender_region_id"),
                @Index(value = "sender_city_id"),
                @Index(value = "recipient_country_id"),
                @Index(value = "recipient_city_id"),
                @Index(value = "provider_id")})
public class Request {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "sender_country_id")
    private Long senderCountryId;

    @Expose
    @SerializedName("region_id")
    @ColumnInfo(name = "sender_region_id")
    private Long senderRegionId;

    @Expose
    @SerializedName("city_id")
    @ColumnInfo(name = "sender_city_id")
    private Long senderCityId;

    @Expose
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private Long userId;

    @Expose
    @SerializedName("client_id")
    @ColumnInfo(name = "client_id")
    private Long clientId;

    @Expose
    @SerializedName("employee_id")
    @ColumnInfo(name = "courier_id")
    private Long courierId;

    @Expose
    @SerializedName("provider_id")
    @ColumnInfo(name = "provider_id")
    private Long providerId;

    @Expose
    @SerializedName("invoice_id")
    @ColumnInfo(name = "invoice_id")
    private Long invoiceId;

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
    @ColumnInfo(name = "recipient_country_id")
    private Long recipientCountryId;

    @Expose
    @SerializedName("city_to")
    @ColumnInfo(name = "recipient_city_id")
    private Long recipientCityId;

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

    @ColumnInfo(name = "is_new")
    private boolean isNew;

    @Ignore
    public Request() {
        this.isNew = true;
    }

    public Request(final long id,
                   final String senderFirstName,
                   final String senderMiddleName,
                   final String senderLastName,
                   final String senderEmail,
                   final String senderPhone,
                   final String senderAddress,
                   final Long senderCountryId,
                   final Long senderRegionId,
                   final Long senderCityId,
                   final Long recipientCountryId,
                   final Long recipientCityId,
                   final String comment,
                   final Long userId,
                   final Long clientId,
                   final Long courierId,
                   final Long providerId,
                   final Long invoiceId,
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
        this.isNew = true;
    }

    public void setSenderCountryId(Long senderCountryId) {
        this.senderCountryId = senderCountryId;
    }

    public void setSenderRegionId(Long senderRegionId) {
        this.senderRegionId = senderRegionId;
    }

    public void setSenderCityId(Long senderCityId) {
        this.senderCityId = senderCityId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
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

    public Long getSenderCountryId() {
        return senderCountryId;
    }

    public void setSenderCountryId(long senderCountryId) {
        this.senderCountryId = senderCountryId;
    }

    public Long getSenderRegionId() {
        return senderRegionId;
    }

    public void setSenderRegionId(long senderRegionId) {
        this.senderRegionId = senderRegionId;
    }

    public Long getSenderCityId() {
        return senderCityId;
    }

    public void setSenderCityId(long senderCityId) {
        this.senderCityId = senderCityId;
    }

    public Long getRecipientCountryId() {
        return recipientCountryId;
    }

    public void setRecipientCountryId(final long recipientCountryId) {
        this.recipientCountryId = recipientCountryId;
    }

    public Long getRecipientCityId() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getInvoiceId() {
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

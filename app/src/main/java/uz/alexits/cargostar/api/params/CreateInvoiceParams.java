package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import uz.alexits.cargostar.model.shipping.Consignment;

public class CreateInvoiceParams {
    @Expose
    @SerializedName("request_id")
    private Long requestId;

    @Expose
    @SerializedName("invoice_id")
    private Long invoiceId;

    @Expose
    @SerializedName("courier_id")
    private long courierId;

    /* Sender */
    @Expose
    @SerializedName("sender_signature")
    private String senderSignature;

    @Expose
    @SerializedName("sender_email")
    private String senderEmail;

    @Expose
    @SerializedName("sender_tnt")
    private String senderTntAccountNumber;

    @Expose
    @SerializedName("sender_fedex")
    private String senderFedexAccountNumber;

    @Expose
    @SerializedName("sender_country_id")
    private long senderCountryId;

    @Expose
    @SerializedName("sender_region_id")
    private long senderRegionId;

    @Expose
    @SerializedName("sender_city_id")
    private long senderCityId;

    @Expose
    @SerializedName("sender_address")
    private String senderAddress;

    @Expose
    @SerializedName("sender_zip")
    private String senderZip;

    @Expose
    @SerializedName("sender_firstname")
    private String senderFirstName;

    @Expose
    @SerializedName("sender_middlename")
    private String senderMiddleName;

    @Expose
    @SerializedName("sender_lastname")
    private String senderLastName;

    @Expose
    @SerializedName("sender_phone")
    private String senderPhone;

    @Expose
    @SerializedName("sender_company")
    private String senderCompanyName;

    @Expose
    @SerializedName("sender_type")
    private int senderType;

    /* Recipient */
    @Expose
    @SerializedName("recipient_signature")
    private String recipientSignature;

    @Expose
    @SerializedName("recipient_email")
    private String recipientEmail;

    @Expose
    @SerializedName("recipient_cargostar")
    private String recipientCargostarAccountNumber;

    @Expose
    @SerializedName("recipient_tnt")
    private String recipientTntAccountNumber;

    @Expose
    @SerializedName("recipient_fedex")
    private String recipientFedexAccountNumber;

    @Expose
    @SerializedName("recipient_country_id")
    private long recipientCountryId;

    @Expose
    @SerializedName("recipient_region_id")
    private long recipientRegionId;

    @Expose
    @SerializedName("recipient_city_id")
    private long recipientCityId;

    @Expose
    @SerializedName("recipient_address")
    private String recipientAddress;

    @Expose
    @SerializedName("recipient_zip")
    private String recipientZip;

    @Expose
    @SerializedName("recipient_firstname")
    private String recipientFirstName;

    @Expose
    @SerializedName("recipient_middlename")
    private String recipientMiddleName;

    @Expose
    @SerializedName("recipient_lastname")
    private String recipientLastName;

    @Expose
    @SerializedName("recipient_phone")
    private String recipientPhone;

    @Expose
    @SerializedName("recipient_company")
    private String recipientCompanyName;

    @Expose
    @SerializedName("recipient_type")
    private int recipientType;

    /* Payer */
    @Expose
    @SerializedName("payer_email")
    private String payerEmail;

    @Expose
    @SerializedName("payer_cargostar")
    private String payerCargostarAccountNumber;

    @Expose
    @SerializedName("payer_tnt")
    private String payerTntAccountNumber;

    @Expose
    @SerializedName("payer_fedex")
    private String payerFedexAccountNumber;

    @Expose
    @SerializedName("payer_country_id")
    private long payerCountryId;

    @Expose
    @SerializedName("payer_region_id")
    private long payerRegionId;

    @Expose
    @SerializedName("payer_city_id")
    private long payerCityId;

    @Expose
    @SerializedName("payer_address")
    private String payerAddress;

    @Expose
    @SerializedName("payer_zip")
    private String payerZip;

    @Expose
    @SerializedName("payer_firstname")
    private String payerFirstName;

    @Expose
    @SerializedName("payer_middlename")
    private String payerMiddleName;

    @Expose
    @SerializedName("payer_lastname")
    private String payerLastName;

    @Expose
    @SerializedName("payer_phone")
    private String payerPhone;

    @Expose
    @SerializedName("payer_inn")
    private String payerInn;

    @Expose
    @SerializedName("payer_company")
    private String payerCompanyName;

    @Expose
    @SerializedName("payer_type")
    private int payerType;

    @Expose
    @SerializedName("payer_tnt_tax_id")
    private String payerTntTaxId;

    @Expose
    @SerializedName("payer_fedex_tax_id")
    private String payerFedexTaxId;

    @Expose
    @SerializedName("discount")
    private double discount;

    /* Payment */
    @Expose
    @SerializedName("account")
    private String checkingAccount;

    @Expose
    @SerializedName("bank")
    private String bank;

    @Expose
    @SerializedName("code")
    private String registrationCode;

    @Expose
    @SerializedName("mfo")
    private String mfo;

    @Expose
    @SerializedName("oked")
    private String oked;

    /* Transportation */
    @Expose
    @SerializedName("transportation_qr")
    private String transportationQr;

    @Expose
    @SerializedName("instructions")
    private String instructions;

    /* CargoList */
    @Expose
    @SerializedName("consignment")
    private List<Consignment> consignmentList;

    /* Calculations */
    @Expose
    @SerializedName("consignment_quantity")
    private int consignmentQuantity;

    @Expose
    @SerializedName("total_weight")
    private double totalWeight;

    @Expose
    @SerializedName("total_volume")
    private double totalVolume;

    @Expose
    @SerializedName("price")
    private double price;

    @Expose
    @SerializedName("packaging_id")
    private long packagingId;

    @Expose
    @SerializedName("total_price")
    private String totalPrice;

    @Expose
    @SerializedName("provider_id")
    private long providerId;

    @Expose
    @SerializedName("delivery_type")
    private int deliveryType;

    @Expose
    @SerializedName("payment_method")
    //1 -> cash & 2-> credit card
    private int paymentMethod;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(final Long requestId) {
        if (requestId == null) {
            return;
        }
        this.requestId = requestId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final Long invoiceId) {
        if (invoiceId == null) {
            return;
        }
        this.invoiceId = invoiceId;
    }

    public long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }

    public String getSenderSignature() {
        return senderSignature;
    }

    public void setSenderSignature(String senderSignature) {
        this.senderSignature = senderSignature;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderTntAccountNumber() {
        return senderTntAccountNumber;
    }

    public void setSenderTntAccountNumber(String senderTntAccountNumber) {
        this.senderTntAccountNumber = senderTntAccountNumber;
    }

    public String getSenderFedexAccountNumber() {
        return senderFedexAccountNumber;
    }

    public void setSenderFedexAccountNumber(String senderFedexAccountNumber) {
        this.senderFedexAccountNumber = senderFedexAccountNumber;
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

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderZip() {
        return senderZip;
    }

    public void setSenderZip(String senderZip) {
        this.senderZip = senderZip;
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

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getRecipientSignature() {
        return recipientSignature;
    }

    public void setRecipientSignature(String recipientSignature) {
        this.recipientSignature = recipientSignature;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientCargostarAccountNumber() {
        return recipientCargostarAccountNumber;
    }

    public void setRecipientCargostarAccountNumber(String recipientCargostarAccountNumber) {
        this.recipientCargostarAccountNumber = recipientCargostarAccountNumber;
    }

    public String getRecipientTntAccountNumber() {
        return recipientTntAccountNumber;
    }

    public void setRecipientTntAccountNumber(String recipientTntAccountNumber) {
        this.recipientTntAccountNumber = recipientTntAccountNumber;
    }

    public String getRecipientFedexAccountNumber() {
        return recipientFedexAccountNumber;
    }

    public void setRecipientFedexAccountNumber(String recipientFedexAccountNumber) {
        this.recipientFedexAccountNumber = recipientFedexAccountNumber;
    }

    public long getRecipientCountryId() {
        return recipientCountryId;
    }

    public void setRecipientCountryId(long recipientCountryId) {
        this.recipientCountryId = recipientCountryId;
    }

    public long getRecipientRegionId() {
        return recipientRegionId;
    }

    public void setRecipientRegionId(long recipientRegionId) {
        this.recipientRegionId = recipientRegionId;
    }

    public long getRecipientCityId() {
        return recipientCityId;
    }

    public void setRecipientCityId(long recipientCityId) {
        this.recipientCityId = recipientCityId;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientZip() {
        return recipientZip;
    }

    public void setRecipientZip(String recipientZip) {
        this.recipientZip = recipientZip;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public String getRecipientMiddleName() {
        return recipientMiddleName;
    }

    public void setRecipientMiddleName(String recipientMiddleName) {
        this.recipientMiddleName = recipientMiddleName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerCargostarAccountNumber() {
        return payerCargostarAccountNumber;
    }

    public void setPayerCargostarAccountNumber(String payerCargostarAccountNumber) {
        this.payerCargostarAccountNumber = payerCargostarAccountNumber;
    }

    public String getPayerTntAccountNumber() {
        return payerTntAccountNumber;
    }

    public void setPayerTntAccountNumber(String payerTntAccountNumber) {
        this.payerTntAccountNumber = payerTntAccountNumber;
    }

    public String getPayerFedexAccountNumber() {
        return payerFedexAccountNumber;
    }

    public void setPayerFedexAccountNumber(String payerFedexAccountNumber) {
        this.payerFedexAccountNumber = payerFedexAccountNumber;
    }

    public long getPayerCountryId() {
        return payerCountryId;
    }

    public void setPayerCountryId(long payerCountryId) {
        this.payerCountryId = payerCountryId;
    }

    public long getPayerRegionId() {
        return payerRegionId;
    }

    public void setPayerRegionId(long payerRegionId) {
        this.payerRegionId = payerRegionId;
    }

    public long getPayerCityId() {
        return payerCityId;
    }

    public void setPayerCityId(long payerCityId) {
        this.payerCityId = payerCityId;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress;
    }

    public String getPayerZip() {
        return payerZip;
    }

    public void setPayerZip(String payerZip) {
        this.payerZip = payerZip;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public void setPayerFirstName(String payerFirstName) {
        this.payerFirstName = payerFirstName;
    }

    public String getPayerMiddleName() {
        return payerMiddleName;
    }

    public void setPayerMiddleName(String payerMiddleName) {
        this.payerMiddleName = payerMiddleName;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public void setPayerLastName(String payerLastName) {
        this.payerLastName = payerLastName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(String checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getMfo() {
        return mfo;
    }

    public void setMfo(String mfo) {
        this.mfo = mfo;
    }

    public String getOked() {
        return oked;
    }

    public void setOked(String oked) {
        this.oked = oked;
    }

    public String getTransportationQr() {
        return transportationQr;
    }

    public void setTransportationQr(String transportationQr) {
        this.transportationQr = transportationQr;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<Consignment> getConsignmentList() {
        return consignmentList;
    }

    public void setConsignmentList(List<Consignment> consignmentList) {
        this.consignmentList = consignmentList;
    }

    public int getConsignmentQuantity() {
        return consignmentQuantity;
    }

    public void setConsignmentQuantity(int consignmentQuantity) {
        this.consignmentQuantity = consignmentQuantity;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getPackagingId() {
        return packagingId;
    }

    public void setPackagingId(long packagingId) {
        this.packagingId = packagingId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSenderCompanyName() {
        return senderCompanyName;
    }

    public void setSenderCompanyName(String senderCompanyName) {
        this.senderCompanyName = senderCompanyName;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public String getRecipientCompanyName() {
        return recipientCompanyName;
    }

    public void setRecipientCompanyName(String recipientCompanyName) {
        this.recipientCompanyName = recipientCompanyName;
    }

    public int getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(int recipientType) {
        this.recipientType = recipientType;
    }

    public String getPayerInn() {
        return payerInn;
    }

    public void setPayerInn(String payerInn) {
        this.payerInn = payerInn;
    }

    public String getPayerCompanyName() {
        return payerCompanyName;
    }

    public void setPayerCompanyName(String payerCompanyName) {
        this.payerCompanyName = payerCompanyName;
    }

    public int getPayerType() {
        return payerType;
    }

    public void setPayerType(int payerType) {
        this.payerType = payerType;
    }

    public String getPayerTntTaxId() {
        return payerTntTaxId;
    }

    public void setPayerTntTaxId(String payerTntTaxId) {
        this.payerTntTaxId = payerTntTaxId;
    }

    public String getPayerFedexTaxId() {
        return payerFedexTaxId;
    }

    public void setPayerFedexTaxId(String payerFedexTaxId) {
        this.payerFedexTaxId = payerFedexTaxId;
    }

    @NonNull
    @Override
    public String toString() {
        return "CreateInvoiceParams{" +
                "requestId=" + requestId +
                ", invoiceId=" + invoiceId +
                ", courierId=" + courierId +
                ", senderSignature='" + senderSignature + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", senderTntAccountNumber='" + senderTntAccountNumber + '\'' +
                ", senderFedexAccountNumber='" + senderFedexAccountNumber + '\'' +
                ", senderCountryId=" + senderCountryId +
                ", senderRegionId=" + senderRegionId +
                ", senderCityId=" + senderCityId +
                ", senderAddress='" + senderAddress + '\'' +
                ", senderZip='" + senderZip + '\'' +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderMiddleName='" + senderMiddleName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderPhone='" + senderPhone + '\'' +
                ", senderCompanyName='" + senderCompanyName + '\'' +
                ", senderType=" + senderType +
                ", recipientSignature='" + recipientSignature + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", recipientCargostarAccountNumber='" + recipientCargostarAccountNumber + '\'' +
                ", recipientTntAccountNumber='" + recipientTntAccountNumber + '\'' +
                ", recipientFedexAccountNumber='" + recipientFedexAccountNumber + '\'' +
                ", recipientCountryId=" + recipientCountryId +
                ", recipientRegionId=" + recipientRegionId +
                ", recipientCityId=" + recipientCityId +
                ", recipientAddress='" + recipientAddress + '\'' +
                ", recipientZip='" + recipientZip + '\'' +
                ", recipientFirstName='" + recipientFirstName + '\'' +
                ", recipientMiddleName='" + recipientMiddleName + '\'' +
                ", recipientLastName='" + recipientLastName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", recipientCompanyName='" + recipientCompanyName + '\'' +
                ", recipientType=" + recipientType +
                ", payerEmail='" + payerEmail + '\'' +
                ", payerCargostarAccountNumber='" + payerCargostarAccountNumber + '\'' +
                ", payerTntAccountNumber='" + payerTntAccountNumber + '\'' +
                ", payerFedexAccountNumber='" + payerFedexAccountNumber + '\'' +
                ", payerCountryId=" + payerCountryId +
                ", payerRegionId=" + payerRegionId +
                ", payerCityId=" + payerCityId +
                ", payerAddress='" + payerAddress + '\'' +
                ", payerZip='" + payerZip + '\'' +
                ", payerFirstName='" + payerFirstName + '\'' +
                ", payerMiddleName='" + payerMiddleName + '\'' +
                ", payerLastName='" + payerLastName + '\'' +
                ", payerPhone='" + payerPhone + '\'' +
                ", payerInn='" + payerInn + '\'' +
                ", payerCompanyName='" + payerCompanyName + '\'' +
                ", payerType=" + payerType +
                ", payerTntTaxId='" + payerTntTaxId + '\'' +
                ", payerFedexTaxId=" + payerFedexTaxId +
                ", discount=" + discount +
                ", checkingAccount='" + checkingAccount + '\'' +
                ", bank='" + bank + '\'' +
                ", registrationCode='" + registrationCode + '\'' +
                ", mfo='" + mfo + '\'' +
                ", oked='" + oked + '\'' +
                ", transportationQr='" + transportationQr + '\'' +
                ", instructions='" + instructions + '\'' +
                ", consignmentList=" + consignmentList +
                ", consignmentQuantity=" + consignmentQuantity +
                ", totalWeight=" + totalWeight +
                ", totalVolume=" + totalVolume +
                ", price=" + price +
                ", packagingId=" + packagingId +
                ", totalPrice=" + totalPrice +
                ", providerId=" + providerId +
                ", deliveryType=" + deliveryType +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}

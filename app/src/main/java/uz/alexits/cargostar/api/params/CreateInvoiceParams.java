package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import uz.alexits.cargostar.model.shipping.Consignment;

public class CreateInvoiceParams {
    @SerializedName("courier_id")
    private final Long courierId;

    @SerializedName("operator_id")
    private final Long operatorId;

    @SerializedName("accountant_id")
    private final Long accountantId;

    /* Sender */
    @SerializedName("sender_signature")
    private final String senderSignature;

    @SerializedName("sender_email")
    private final String senderEmail;

    @SerializedName("sender_cargostar")
    private final String senderCargostarAccountNumber;

    @SerializedName("sender_tnt")
    private final String senderTntAccountNumber;

    @SerializedName("sender_fedex")
    private final String senderFedexAccountNumber;

    @SerializedName("sender_country")
    private final String senderCountry;

    @SerializedName("sender_region")
    private final String senderRegion;

    @SerializedName("sender_city")
    private final String senderCity;

    @SerializedName("sender_address")
    private final String senderAddress;

    @SerializedName("sender_zip")
    private final String senderZip;

    @SerializedName("sender_firstname")
    private final String senderFirstName;

    @SerializedName("sender_middlename")
    private final String senderMiddleName;

    @SerializedName("sender_lastname")
    private final String senderLastName;

    @SerializedName("sender_phone")
    private final String senderPhone;

    /* Recipient */
    @SerializedName("recipient_signature")
    private final String recipientSignature;

    @SerializedName("recipient_email")
    private final String recipientEmail;

    @SerializedName("recipient_cargostar")
    private final String recipientCargostarAccountNumber;

    @SerializedName("recipient_tnt")
    private final String recipientTntAccountNumber;

    @SerializedName("recipient_fedex")
    private final String recipientFedexAccountNumber;

    @SerializedName("recipient_country")
    private final String recipientCountry;

    @SerializedName("recipient_region")
    private final String recipientRegion;

    @SerializedName("recipient_city")
    private final String recipientCity;

    @SerializedName("recipient_address")
    private final String recipientAddress;

    @SerializedName("recipient_zip")
    private final String recipientZip;

    @SerializedName("recipient_firstname")
    private final String recipientFirstName;

    @SerializedName("recipient_middlename")
    private final String recipientMiddleName;

    @SerializedName("recipient_lastname")
    private final String recipientLastName;

    @SerializedName("recipient_phone")
    private final String recipientPhone;

    /* Payer */
    @SerializedName("payer_email")
    private final String payerEmail;

    @SerializedName("payer_cargostar")
    private final String payerCargostarAccountNumber;

    @SerializedName("payer_tnt")
    private final String payerTntAccountNumber;

    @SerializedName("payer_fedex")
    private final String payerFedexAccountNumber;

    @SerializedName("payer_country")
    private final String payerCountry;

    @SerializedName("payer_region")
    private final String payerRegion;

    @SerializedName("payer_city")
    private final String payerCity;

    @SerializedName("payer_address")
    private final String payerAddress;

    @SerializedName("payer_zip")
    private final String payerZip;

    @SerializedName("payer_firstname")
    private final String payerFirstName;

    @SerializedName("payer_middlename")
    private final String payerMiddleName;

    @SerializedName("payer_lastname")
    private final String payerLastName;

    @SerializedName("payer_phone")
    private final String payerPhone;

    @SerializedName("discount")
    private final String discount;

    /* Payment */
    @SerializedName("account")
    private final String checkingAccount;

    @SerializedName("bank")
    private final String bank;

    @SerializedName("code")
    private final String registrationCode;

    @SerializedName("mfo")
    private final String mfo;

    @SerializedName("oked")
    private final String oked;

    /* Transportation */
    @SerializedName("transportation_qr")
    private final String qr;

    @SerializedName("instructions")
    private final String courierGuidelines;

    /* CargoList */
    @SerializedName("cargo")
    private final List<Consignment> consignmentList;

    @SerializedName("price")
    private final double price;

    @SerializedName("tariff_id")
    private final Long tariffId;

    @SerializedName("provider_id")
    private final Long providerId;

    @SerializedName("payment_method")
    private final int paymentMethod;


    public CreateInvoiceParams(
            final Long courierId,
            final Long operatorId,
            final Long accountantId,
            final String senderSignature,
            final String senderEmail,
            final String senderCargostarAccountNumber,
            final String senderTntAccountNumber,
            final String senderFedexAccountNumber,
            final String senderCountry,
            final String senderRegion,
            final String senderCity,
            final String senderAddress,
            final String senderZip,
            final String senderFirstName,
            final String senderMiddleName,
            final String senderLastName,
            final String senderPhone,
            final String recipientSignature,
            final String recipientEmail,
            final String recipientCargostarAccountNumber,
            final String recipientTntAccountNumber,
            final String recipientFedexAccountNumber,
            final String recipientCountry,
            final String recipientRegion,
            final String recipientCity,
            final String recipientAddress,
            final String recipientZip,
            final String recipientFirstName,
            final String recipientMiddleName,
            final String recipientLastName,
            final String recipientPhone,
            final String payerEmail,
            final String payerCargostarAccountNumber,
            final String payerTntAccountNumber,
            final String payerFedexAccountNumber,
            final String payerCountry,
            final String payerRegion,
            final String payerCity,
            final String payerAddress,
            final String payerZip,
            final String payerFirstName,
            final String payerMiddleName,
            final String payerLastName,
            final String payerPhone,
            final String discount,
            final String checkingAccount,
            final String bank,
            final String registrationCode,
            final String mfo,
            final String oked,
            final String qr,
            final String courierGuidelines,
            final List<Consignment> consignmentList,
            final double price,
            final Long tariffId,
            final Long providerId,
            final int paymentMethod) {
        this.courierId = courierId;
        this.operatorId = operatorId;
        this.accountantId = accountantId;
        this.senderSignature = senderSignature;
        this.senderEmail = senderEmail;
        this.senderCargostarAccountNumber = senderCargostarAccountNumber;
        this.senderTntAccountNumber = senderTntAccountNumber;
        this.senderFedexAccountNumber = senderFedexAccountNumber;
        this.senderCountry = senderCountry;
        this.senderRegion = senderRegion;
        this.senderCity = senderCity;
        this.senderAddress = senderAddress;
        this.senderZip = senderZip;
        this.senderFirstName = senderFirstName;
        this.senderMiddleName = senderMiddleName;
        this.senderLastName = senderLastName;
        this.senderPhone = senderPhone;
        this.recipientSignature = recipientSignature;
        this.recipientEmail = recipientEmail;
        this.recipientCargostarAccountNumber = recipientCargostarAccountNumber;
        this.recipientTntAccountNumber = recipientTntAccountNumber;
        this.recipientFedexAccountNumber = recipientFedexAccountNumber;
        this.recipientCountry = recipientCountry;
        this.recipientRegion = recipientRegion;
        this.recipientCity = recipientCity;
        this.recipientAddress = recipientAddress;
        this.recipientZip = recipientZip;
        this.recipientFirstName = recipientFirstName;
        this.recipientMiddleName = recipientMiddleName;
        this.recipientLastName = recipientLastName;
        this.recipientPhone = recipientPhone;
        this.payerEmail = payerEmail;
        this.payerCargostarAccountNumber = payerCargostarAccountNumber;
        this.payerTntAccountNumber = payerTntAccountNumber;
        this.payerFedexAccountNumber = payerFedexAccountNumber;
        this.payerCountry = payerCountry;
        this.payerRegion = payerRegion;
        this.payerCity = payerCity;
        this.payerAddress = payerAddress;
        this.payerZip = payerZip;
        this.payerFirstName = payerFirstName;
        this.payerMiddleName = payerMiddleName;
        this.payerLastName = payerLastName;
        this.payerPhone = payerPhone;
        this.discount = discount;
        this.checkingAccount = checkingAccount;
        this.bank = bank;
        this.registrationCode = registrationCode;
        this.mfo = mfo;
        this.oked = oked;
        this.qr = qr;
        this.courierGuidelines = courierGuidelines;
        this.consignmentList = consignmentList;
        this.price = price;
        this.tariffId = tariffId;
        this.providerId = providerId;
        this.paymentMethod = paymentMethod;
    }

    public Long getCourierId() {
        return courierId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public Long getAccountantId() {
        return accountantId;
    }

    public String getSenderSignature() {
        return senderSignature;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderCargostarAccountNumber() {
        return senderCargostarAccountNumber;
    }

    public String getSenderTntAccountNumber() {
        return senderTntAccountNumber;
    }

    public String getSenderFedexAccountNumber() {
        return senderFedexAccountNumber;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public String getSenderRegion() {
        return senderRegion;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getSenderZip() {
        return senderZip;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public String getSenderMiddleName() {
        return senderMiddleName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getRecipientSignature() {
        return recipientSignature;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getRecipientCargostarAccountNumber() {
        return recipientCargostarAccountNumber;
    }

    public String getRecipientTntAccountNumber() {
        return recipientTntAccountNumber;
    }

    public String getRecipientFedexAccountNumber() {
        return recipientFedexAccountNumber;
    }

    public String getRecipientCountry() {
        return recipientCountry;
    }

    public String getRecipientRegion() {
        return recipientRegion;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getRecipientZip() {
        return recipientZip;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public String getRecipientMiddleName() {
        return recipientMiddleName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getPayerCargostarAccountNumber() {
        return payerCargostarAccountNumber;
    }

    public String getPayerTntAccountNumber() {
        return payerTntAccountNumber;
    }

    public String getPayerFedexAccountNumber() {
        return payerFedexAccountNumber;
    }

    public String getPayerCountry() {
        return payerCountry;
    }

    public String getPayerRegion() {
        return payerRegion;
    }

    public String getPayerCity() {
        return payerCity;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public String getPayerZip() {
        return payerZip;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public String getPayerMiddleName() {
        return payerMiddleName;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public String getDiscount() {
        return discount;
    }

    public String getCheckingAccount() {
        return checkingAccount;
    }

    public String getBank() {
        return bank;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public String getMfo() {
        return mfo;
    }

    public String getOked() {
        return oked;
    }

    public String getQr() {
        return qr;
    }

    public String getCourierGuidelines() {
        return courierGuidelines;
    }

    public List<Consignment> getConsignmentList() {
        return consignmentList;
    }

    public double getPrice() {
        return price;
    }

    public Long getTariffId() {
        return tariffId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    @NonNull
    @Override
    public String toString() {
        return "CreateInvoiceParams{" +
                "courierId=" + courierId +
                ", operatorId=" + operatorId +
                ", accountantId=" + accountantId +
                ", senderSignature='" + senderSignature + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", senderCargostarAccountNumber='" + senderCargostarAccountNumber + '\'' +
                ", senderTntAccountNumber='" + senderTntAccountNumber + '\'' +
                ", senderFedexAccountNumber='" + senderFedexAccountNumber + '\'' +
                ", senderCountry='" + senderCountry + '\'' +
                ", senderRegion='" + senderRegion + '\'' +
                ", senderCity='" + senderCity + '\'' +
                ", senderAddress='" + senderAddress + '\'' +
                ", senderZip='" + senderZip + '\'' +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderMiddleName='" + senderMiddleName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderPhone='" + senderPhone + '\'' +
                ", recipientSignature='" + recipientSignature + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", recipientCargostarAccountNumber='" + recipientCargostarAccountNumber + '\'' +
                ", recipientTntAccountNumber='" + recipientTntAccountNumber + '\'' +
                ", recipientFedexAccountNumber='" + recipientFedexAccountNumber + '\'' +
                ", recipientCountry='" + recipientCountry + '\'' +
                ", recipientRegion='" + recipientRegion + '\'' +
                ", recipientCity='" + recipientCity + '\'' +
                ", recipientAddress='" + recipientAddress + '\'' +
                ", recipientZip='" + recipientZip + '\'' +
                ", recipientFirstName='" + recipientFirstName + '\'' +
                ", recipientMiddleName='" + recipientMiddleName + '\'' +
                ", recipientLastName='" + recipientLastName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", payerEmail='" + payerEmail + '\'' +
                ", payerCargostarAccountNumber='" + payerCargostarAccountNumber + '\'' +
                ", payerTntAccountNumber='" + payerTntAccountNumber + '\'' +
                ", payerFedexAccountNumber='" + payerFedexAccountNumber + '\'' +
                ", payerCountry='" + payerCountry + '\'' +
                ", payerRegion='" + payerRegion + '\'' +
                ", payerCity='" + payerCity + '\'' +
                ", payerAddress='" + payerAddress + '\'' +
                ", payerZip='" + payerZip + '\'' +
                ", payerFirstName='" + payerFirstName + '\'' +
                ", payerMiddleName='" + payerMiddleName + '\'' +
                ", payerLastName='" + payerLastName + '\'' +
                ", payerPhone='" + payerPhone + '\'' +
                ", discount=" + discount +
                ", checkingAccount='" + checkingAccount + '\'' +
                ", bank='" + bank + '\'' +
                ", registrationCode='" + registrationCode + '\'' +
                ", mfo='" + mfo + '\'' +
                ", oked='" + oked + '\'' +
                ", qr='" + qr + '\'' +
                ", courierGuidelines='" + courierGuidelines + '\'' +
                ", cargoList=" + consignmentList +
                ", price=" + price +
                ", tariffId='" + tariffId + '\'' +
                ", providerId=" + providerId +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}

package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import uz.alexits.cargostar.model.Document;
import uz.alexits.cargostar.model.PaymentStatus;
import uz.alexits.cargostar.model.TransportationStatus;
import uz.alexits.cargostar.model.location.Address;
import uz.alexits.cargostar.model.location.TransitPoint;

import java.util.Date;

@Entity(tableName = "receipt",
        foreignKeys = {@ForeignKey(entity = TransitPoint.class, parentColumns = "id", childColumns = "current_location_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "courier_id", unique = false),
                @Index(value = "qr", unique = true),
                @Index(value = "tracking_code", unique = true),
                @Index(value = "current_location_id"),
                @Index(value = "consolidation_number", unique = false)})
public class Receipt {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "courier_id", defaultValue = "-1") private long courierId;
    @ColumnInfo(name = "service_provider") @Nullable private String serviceProvider;
    //can be list
    @ColumnInfo(name = "operator_id") private String operatorId;
    //can be list
    @ColumnInfo(name = "accountant_id") @Nullable private String accountantId;

    @ColumnInfo(name = "sender_login") @Nullable private String senderLogin;
    @ColumnInfo(name = "sender_signature") @Nullable private String senderSignature;
    @Embedded(prefix = "sender_")  private @NonNull Address senderAddress;
    @ColumnInfo(name = "sender_first_name") @NonNull private String senderFirstName;
    @ColumnInfo(name = "sender_middle_name") @NonNull private String senderMiddleName;
    @ColumnInfo(name = "sender_last_name") @NonNull private String senderLastName;
    @ColumnInfo(name = "sender_phone") @NonNull private String senderPhone;
    @ColumnInfo(name = "sender_email") @Nullable private String senderEmail;
    @ColumnInfo(name = "sender_cargostar_acc_num") @Nullable private String senderCargostarAccountNumber;
    @ColumnInfo(name = "sender_tnt_acc_num") @Nullable private String senderTntAccountNumber;
    @ColumnInfo(name = "sender_fedex_acc_num") @Nullable private String senderFedexAccountNumber;

    @ColumnInfo(name = "recipient_login") @Nullable private String recipientLogin;
    @ColumnInfo(name = "recipient_signature") @Nullable private String recipientSignature;
    @Embedded(prefix = "recipient_") @Nullable private Address recipientAddress;
    @ColumnInfo(name = "recipient_first_name") @Nullable private String recipientFirstName;
    @ColumnInfo(name = "recipient_middle_name") @Nullable private String recipientMiddleName;
    @ColumnInfo(name = "recipient_last_name") @Nullable private String recipientLastName;
    @ColumnInfo(name = "recipient_phone") @Nullable private String recipientPhone;
    @ColumnInfo(name = "recipient_email") @Nullable private String recipientEmail;
    @ColumnInfo(name = "recipient_cargostar_acc_num") @Nullable private String recipientCargostarAccountNumber;
    @ColumnInfo(name = "recipient_tnt_acc_num") @Nullable private String recipientTntAccountNumber;
    @ColumnInfo(name = "recipient_fedex_acc_num") @Nullable private String recipientFedexAccountNumber;

    @ColumnInfo(name = "payer_login") @Nullable private String payerLogin;
    @Embedded(prefix = "payer_") @Nullable private Address payerAddress;
    @ColumnInfo(name = "payer_first_name") @Nullable private String payerFirstName;
    @ColumnInfo(name = "payer_middle_name") @Nullable private String payerMiddleName;
    @ColumnInfo(name = "payer_last_name") @Nullable private String payerLastName;
    @ColumnInfo(name = "payer_phone") @Nullable private String payerPhone;
    @ColumnInfo(name = "payer_email") @Nullable private String payerEmail;
    @ColumnInfo(name = "payer_cargostar_acc_num") @Nullable private String payerCargostarAccountNumber;
    @ColumnInfo(name = "payer_tnt_acc_num") @Nullable private String payerTntAccountNumber;
    @ColumnInfo(name = "payer_fedex_acc_num") @Nullable private String payerFedexAccountNumber;
    @ColumnInfo(name = "discount", defaultValue = "-1") private int discount;
    @ColumnInfo(name = "checking_account") @Nullable private String checkingAccount;
    @ColumnInfo(name = "bank") @Nullable private String bank;
    @ColumnInfo(name = "registration_code") @Nullable private String registrationCode;
    @ColumnInfo(name = "mfo") @Nullable private String mfo;
    @ColumnInfo(name = "oked") @Nullable private String oked;
    @ColumnInfo(name = "instructions") @Nullable private String instructions;

    @ColumnInfo(name = "tracking_code") @Nullable private String trackingCode;
    @ColumnInfo(name = "qr") @Nullable private String qr;

    @ColumnInfo(name = "tariff") @Nullable private String tariff;
    @ColumnInfo(name = "cost", defaultValue = "-1") private long cost;
    @ColumnInfo(name = "fuel_charge", defaultValue = "-1") private int fuelCharge;
    @ColumnInfo(name = "vat", defaultValue = "-1") private int vat;

    @ColumnInfo(name = "dispatch_date") @Nullable private Date dispatchDate;
    @ColumnInfo(name = "arrival_date") @Nullable private Date arrivalDate;

    @Embedded(prefix = "request_") @Nullable private Document request;
    @Embedded(prefix = "receipt_") @Nullable private Document receipt;
    @Embedded(prefix = "guarantee_letter_") @Nullable private Document guaranteeLetter;
    @Embedded(prefix = "paybill_") @Nullable private Document paybill;
    @Embedded(prefix = "waybill_") @Nullable private Document waybill;
    @Embedded(prefix = "invoice_") @Nullable private Document invoice;

    @ColumnInfo(name = "current_location_id") private Long currentLocation;

    @ColumnInfo(name = "transportation_status") @NonNull private TransportationStatus transportationStatus;
    @ColumnInfo(name = "payment_status") @NonNull private PaymentStatus paymentStatus;
    @ColumnInfo(name = "is_read", defaultValue = "true") private boolean isRead;

    @ColumnInfo(name = "consolidation_number", defaultValue = "-1") private long consolidationNumber;

    public Receipt(@NonNull final String senderFirstName,
                   @NonNull final String senderMiddleName,
                   @NonNull final String senderLastName,
                   @NonNull final Address senderAddress,
                   @NonNull final String senderPhone,
                   @NonNull final TransportationStatus transportationStatus,
                   @NonNull final PaymentStatus paymentStatus) {
        this.senderAddress = senderAddress;
        this.senderFirstName = senderFirstName;
        this.senderMiddleName = senderMiddleName;
        this.senderLastName = senderLastName;
        this.senderPhone = senderPhone;
        this.transportationStatus = transportationStatus;
        this.paymentStatus = paymentStatus;
        this.courierId = -1;
        this.discount = -1;
        this.cost = -1;
        this.fuelCharge = -1;
        this.vat = -1;
        this.consolidationNumber = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourierId() {
        return courierId;
    }

    public void setCourierId(long courierId) {
        this.courierId = courierId;
    }

    @Nullable
    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(@Nullable String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @NonNull
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(@NonNull String operatorId) {
        this.operatorId = operatorId;
    }

    @Nullable
    public String getAccountantId() {
        return accountantId;
    }

    public void setAccountantId(@Nullable String accountantId) {
        this.accountantId = accountantId;
    }

    @Nullable
    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(@Nullable String senderLogin) {
        this.senderLogin = senderLogin;
    }

    @Nullable
    public String getSenderSignature() {
        return senderSignature;
    }

    public void setSenderSignature(@Nullable String senderSignature) {
        this.senderSignature = senderSignature;
    }

    @NonNull
    public Address getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(@NonNull Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    @NonNull
    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(@NonNull String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    @NonNull
    public String getSenderMiddleName() {
        return senderMiddleName;
    }

    public void setSenderMiddleName(@NonNull String senderMiddleName) {
        this.senderMiddleName = senderMiddleName;
    }

    @NonNull
    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(@NonNull String senderLastName) {
        this.senderLastName = senderLastName;
    }

    @NonNull
    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(@NonNull String senderPhone) {
        this.senderPhone = senderPhone;
    }

    @Nullable
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(@Nullable String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Nullable
    public String getSenderCargostarAccountNumber() {
        return senderCargostarAccountNumber;
    }

    public void setSenderCargostarAccountNumber(@Nullable String senderCargostarAccountNumber) {
        this.senderCargostarAccountNumber = senderCargostarAccountNumber;
    }

    @Nullable
    public String getSenderTntAccountNumber() {
        return senderTntAccountNumber;
    }

    public void setSenderTntAccountNumber(@Nullable String senderTntAccountNumber) {
        this.senderTntAccountNumber = senderTntAccountNumber;
    }

    @Nullable
    public String getSenderFedexAccountNumber() {
        return senderFedexAccountNumber;
    }

    public void setSenderFedexAccountNumber(@Nullable String senderFedexAccountNumber) {
        this.senderFedexAccountNumber = senderFedexAccountNumber;
    }

    @Nullable
    public String getRecipientLogin() {
        return recipientLogin;
    }

    public void setRecipientLogin(@Nullable String recipientLogin) {
        this.recipientLogin = recipientLogin;
    }

    @Nullable
    public String getRecipientSignature() {
        return recipientSignature;
    }

    public void setRecipientSignature(@Nullable String recipientSignature) {
        this.recipientSignature = recipientSignature;
    }

    @Nullable
    public Address getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(@Nullable Address recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Nullable
    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(@Nullable String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    @Nullable
    public String getRecipientMiddleName() {
        return recipientMiddleName;
    }

    public void setRecipientMiddleName(@Nullable String recipientMiddleName) {
        this.recipientMiddleName = recipientMiddleName;
    }

    @Nullable
    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(@Nullable String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    @Nullable
    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(@Nullable String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    @Nullable
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(@Nullable String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Nullable
    public String getRecipientCargostarAccountNumber() {
        return recipientCargostarAccountNumber;
    }

    public void setRecipientCargostarAccountNumber(@Nullable String recipientCargostarAccountNumber) {
        this.recipientCargostarAccountNumber = recipientCargostarAccountNumber;
    }

    @Nullable
    public String getRecipientTntAccountNumber() {
        return recipientTntAccountNumber;
    }

    public void setRecipientTntAccountNumber(@Nullable String recipientTntAccountNumber) {
        this.recipientTntAccountNumber = recipientTntAccountNumber;
    }

    @Nullable
    public String getRecipientFedexAccountNumber() {
        return recipientFedexAccountNumber;
    }

    public void setRecipientFedexAccountNumber(@Nullable String recipientFedexAccountNumber) {
        this.recipientFedexAccountNumber = recipientFedexAccountNumber;
    }

    @Nullable
    public String getPayerLogin() {
        return payerLogin;
    }

    public void setPayerLogin(@Nullable String payerLogin) {
        this.payerLogin = payerLogin;
    }

    @Nullable
    public Address getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(@Nullable Address payerAddress) {
        this.payerAddress = payerAddress;
    }

    @Nullable
    public String getPayerFirstName() {
        return payerFirstName;
    }

    public void setPayerFirstName(@Nullable String payerFirstName) {
        this.payerFirstName = payerFirstName;
    }

    @Nullable
    public String getPayerMiddleName() {
        return payerMiddleName;
    }

    public void setPayerMiddleName(@Nullable String payerMiddleName) {
        this.payerMiddleName = payerMiddleName;
    }

    @Nullable
    public String getPayerLastName() {
        return payerLastName;
    }

    public void setPayerLastName(@Nullable String payerLastName) {
        this.payerLastName = payerLastName;
    }

    @Nullable
    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(@Nullable String payerPhone) {
        this.payerPhone = payerPhone;
    }

    @Nullable
    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(@Nullable String payerEmail) {
        this.payerEmail = payerEmail;
    }

    @Nullable
    public String getPayerCargostarAccountNumber() {
        return payerCargostarAccountNumber;
    }

    public void setPayerCargostarAccountNumber(@Nullable String payerCargostarAccountNumber) {
        this.payerCargostarAccountNumber = payerCargostarAccountNumber;
    }

    @Nullable
    public String getPayerTntAccountNumber() {
        return payerTntAccountNumber;
    }

    public void setPayerTntAccountNumber(@Nullable String payerTntAccountNumber) {
        this.payerTntAccountNumber = payerTntAccountNumber;
    }

    @Nullable
    public String getPayerFedexAccountNumber() {
        return payerFedexAccountNumber;
    }

    public void setPayerFedexAccountNumber(@Nullable String payerFedexAccountNumber) {
        this.payerFedexAccountNumber = payerFedexAccountNumber;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Nullable
    public String getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(@Nullable String checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    @Nullable
    public String getBank() {
        return bank;
    }

    public void setBank(@Nullable String bank) {
        this.bank = bank;
    }

    @Nullable
    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(@Nullable String registrationCode) {
        this.registrationCode = registrationCode;
    }

    @Nullable
    public String getMfo() {
        return mfo;
    }

    public void setMfo(@Nullable String mfo) {
        this.mfo = mfo;
    }

    @Nullable
    public String getOked() {
        return oked;
    }

    public void setOked(@Nullable String oked) {
        this.oked = oked;
    }

    @Nullable
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(@Nullable String instructions) {
        this.instructions = instructions;
    }

    @Nullable
    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(@Nullable String trackingCode) {
        this.trackingCode = trackingCode;
    }

    @Nullable
    public String getQr() {
        return qr;
    }

    public void setQr(@Nullable String qr) {
        this.qr = qr;
    }

    @Nullable
    public String getTariff() {
        return tariff;
    }

    public void setTariff(@Nullable String tariff) {
        this.tariff = tariff;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public int getFuelCharge() {
        return fuelCharge;
    }

    public void setFuelCharge(int fuelCharge) {
        this.fuelCharge = fuelCharge;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    @Nullable
    public Date getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(@Nullable Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    @Nullable
    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(@Nullable Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Nullable
    public Document getReceipt() {
        return receipt;
    }

    public void setReceipt(@Nullable Document receipt) {
        this.receipt = receipt;
    }

    @Nullable
    public Document getInvoice() {
        return invoice;
    }

    public void setInvoice(@Nullable Document invoice) {
        this.invoice = invoice;
    }

    @Nullable
    public Document getRequest() {
        return request;
    }

    public void setRequest(@Nullable Document request) {
        this.request = request;
    }

    @Nullable
    public Document getGuaranteeLetter() {
        return guaranteeLetter;
    }

    public void setGuaranteeLetter(@Nullable Document guaranteeLetter) {
        this.guaranteeLetter = guaranteeLetter;
    }

    @Nullable
    public Document getWaybill() {
        return waybill;
    }

    public void setWaybill(@Nullable Document waybill) {
        this.waybill = waybill;
    }

    @Nullable
    public Document getPaybill() {
        return paybill;
    }

    public void setPaybill(@Nullable Document paybill) {
        this.paybill = paybill;
    }

    @NonNull
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(@NonNull PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @NonNull
    public TransportationStatus getTransportationStatus() {
        return transportationStatus;
    }

    public void setTransportationStatus(@NonNull TransportationStatus transportationStatus) {
        this.transportationStatus = transportationStatus;
    }

    public Long getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Long currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean aRead) {
        isRead = aRead;
    }

    public long getConsolidationNumber() {
        return consolidationNumber;
    }

    public void setConsolidationNumber(long consolidationNumber) {
        this.consolidationNumber = consolidationNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "Receipt{" +
                "id='" + id + '\'' +
                ", isRead='" + isRead + '\'' +
                ", serviceProvider='" + serviceProvider + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", accountantId='" + accountantId + '\'' +
                ", senderLogin='" + senderLogin + '\'' +
                ", senderSignature='" + senderSignature + '\'' +
                ", senderAddress=" + senderAddress +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderMiddleName='" + senderMiddleName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderPhone='" + senderPhone + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", senderCargostarAccountNumber='" + senderCargostarAccountNumber + '\'' +
                ", senderTntAccountNumber='" + senderTntAccountNumber + '\'' +
                ", senderFedexAccountNumber='" + senderFedexAccountNumber + '\'' +
                ", recipientLogin='" + recipientLogin + '\'' +
                ", recipientSignature='" + recipientSignature + '\'' +
                ", recipientAddress=" + recipientAddress +
                ", recipientFirstName='" + recipientFirstName + '\'' +
                ", recipientMiddleName='" + recipientMiddleName + '\'' +
                ", recipientLastName='" + recipientLastName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", recipientCargostarAccountNumber='" + recipientCargostarAccountNumber + '\'' +
                ", recipientTntAccountNumber='" + recipientTntAccountNumber + '\'' +
                ", recipientFedexAccountNumber='" + recipientFedexAccountNumber + '\'' +
                ", payerLogin='" + payerLogin + '\'' +
                ", payerAddress=" + payerAddress +
                ", payerFirstName='" + payerFirstName + '\'' +
                ", payerMiddleName='" + payerMiddleName + '\'' +
                ", payerLastName='" + payerLastName + '\'' +
                ", payerPhone='" + payerPhone + '\'' +
                ", payerEmail='" + payerEmail + '\'' +
                ", payerCargostarAccountNumber='" + payerCargostarAccountNumber + '\'' +
                ", payerTntAccountNumber='" + payerTntAccountNumber + '\'' +
                ", payerFedexAccountNumber='" + payerFedexAccountNumber + '\'' +
                ", discount=" + discount +
                ", checkingAccount='" + checkingAccount + '\'' +
                ", bank='" + bank + '\'' +
                ", registrationCode='" + registrationCode + '\'' +
                ", mfo='" + mfo + '\'' +
                ", oked='" + oked + '\'' +
                ", instructions='" + instructions + '\'' +
                ", trackingCode='" + trackingCode + '\'' +
                ", qr='" + qr + '\'' +
                ", tariff='" + tariff + '\'' +
                ", cost=" + cost +
                ", fuelCharge=" + fuelCharge +
                ", vat=" + vat +
                ", dispatchDate=" + dispatchDate +
                ", arrivalDate=" + arrivalDate +
                ", receipt=" + receipt +
                ", invoice=" + invoice +
                ", request=" + request +
                ", guaranteeLetter=" + guaranteeLetter +
                ", waybill=" + waybill +
                ", bill=" + paybill +
                ", paymentStatus=" + paymentStatus +
                ", consolidationNumber=" + consolidationNumber +
                '}';
    }
}

package uz.alexits.cargostar.database.converters;

import androidx.room.TypeConverter;
import uz.alexits.cargostar.entities.PaymentStatus;

public class PaymentStatusConverter {
    @TypeConverter
    public static int paymentStatusToInt(final PaymentStatus status) {
        if (status == PaymentStatus.WAITING_PAYMENT) {
            return 0;
        }
        if (status == PaymentStatus.WAITING_CHECK) {
            return 1;
        }
        if (status == PaymentStatus.PAID_PARTIALLY) {
            return 2;
        }
        if (status == PaymentStatus.PAID) {
            return 3;
        }
        if (status == PaymentStatus.PAID_MORE) {
            return 4;
        }
        return 0;
    }

    @TypeConverter
    public static PaymentStatus intToPaymentStatus(final int value) {
        if (value == 0) {
            return PaymentStatus.WAITING_PAYMENT;
        }
        if (value == 1) {
            return PaymentStatus.WAITING_CHECK;
        }
        if (value == 2) {
            return PaymentStatus.PAID_PARTIALLY;
        }
        if (value == 3) {
            return PaymentStatus.PAID;
        }
        if (value == 4) {
            return PaymentStatus.PAID_MORE;
        }
        return null;
    }
}

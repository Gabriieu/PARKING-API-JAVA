package app.vercel.josegabriel.parking_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    private static final double DESCONTO_PERCENTUAL = 0.30;

    public static String generateReceipt(String plate) {
        LocalDateTime dateTime = LocalDateTime.now();
        String receipt = dateTime.toString().substring(0, 19);

        return plate.replace("-", "") + "-" + receipt.replace("-", "").replace("T", "-").replace(":", "");
    }

    public static BigDecimal calculateValue(LocalDateTime entryTime, LocalDateTime existTime) {
        long minutes = entryTime.until(existTime, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES;
        } else if (minutes <= 60) {
            total = PRIMEIROS_60_MINUTES;
        } else {
            total = Math.ceil(((minutes - 60.0) / 15.0)) * ADICIONAL_15_MINUTES + PRIMEIROS_60_MINUTES;
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal cost, long totalParkingVisits) {
        BigDecimal desconto = ((totalParkingVisits > 0) && (totalParkingVisits % 10 == 0)) ? cost.multiply(new BigDecimal(DESCONTO_PERCENTUAL)) : new BigDecimal(0);
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}

package com.anudip.HealthcareSystem.utility;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotUtil {
    public static List<LocalTime> getAvailableSlots() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(10, 0); // 10 AM
        LocalTime end = LocalTime.of(18, 0);   // 6 PM

        while (start.isBefore(end)) {
            if (!start.equals(LocalTime.of(13, 0))) { // Skip 1-2 PM
                slots.add(start);
            }
            start = start.plusHours(1);
        }
        return slots;
    }
}
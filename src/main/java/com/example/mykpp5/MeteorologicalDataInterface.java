package com.example.mykpp5;

import java.util.Date;
import java.util.List;

public interface MeteorologicalDataInterface {
    void addObservation(Date observationDate, Double temperature, Double pressure);
    List<MeteorologicalObservations> getAllObservations();
    List<MeteorologicalObservations> getDaysWithLargestPressureDifference();
    void deleteObservationById(int id);
    void updateObservation(int id, Date observationDate, Double temperature, Double pressure);
}

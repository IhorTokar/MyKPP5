package com.example.mykpp5;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeteorologicalObservations implements MeteorologicalDataInterface {
    private static final List<MeteorologicalObservations> observationsData = new ArrayList<>();
    private Integer ID;
    private Date observationDate;
    private Double temperature;
    private Double pressure;
    // Конструктор
    public MeteorologicalObservations(){}
    public MeteorologicalObservations(Integer id, Date observationDate, Double temperature, Double pressure) {
        this.ID = id;
        this.observationDate = observationDate;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    @Override
    public void addObservation(Date observationDate, Double temperature, Double pressure) {
        String sql = "INSERT INTO Observations (observation_date, temperature, pressure) VALUES (?, ?, ?)";
        try (Connection connection = DBConnectionLab5_1.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDate(1, new java.sql.Date(observationDate.getTime()));
            preparedStatement.setDouble(2, temperature);
            preparedStatement.setDouble(3, pressure);

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer generatedId = generatedKeys.getInt(1);
                    observationsData.add(new MeteorologicalObservations(generatedId, observationDate, temperature, pressure));
                } else {
                    throw new SQLException("Не вдалося отримати згенерований ID.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MeteorologicalObservations> getAllObservations() {
        observationsData.clear();
        String sql = "SELECT * FROM Observations ORDER BY pressure ASC";
        try (Connection connection = DBConnectionLab5_1.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Date observationDate = resultSet.getDate("observation_date");
                Double temperature = resultSet.getDouble("temperature");
                Double pressure = resultSet.getDouble("pressure");

                observationsData.add(new MeteorologicalObservations(id, observationDate, temperature, pressure));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(observationsData);
    }

    @Override
    public List<MeteorologicalObservations> getDaysWithLargestPressureDifference() {
        List<MeteorologicalObservations> largestDifferenceDays = new ArrayList<>();
        double maxDifference = 0.0;
        MeteorologicalObservations previousObservation = null;

        for (MeteorologicalObservations currentObservation : observationsData) {
            if (previousObservation != null) {
                double difference = Math.abs(currentObservation.getPressure() - previousObservation.getPressure());
                if (difference > maxDifference) {
                    maxDifference = difference;
                    largestDifferenceDays.clear();
                    largestDifferenceDays.add(previousObservation);
                    largestDifferenceDays.add(currentObservation);
                }
            }
            previousObservation = currentObservation;
        }
        return largestDifferenceDays;
    }
    @Override
    public void deleteObservationById(int id) {
        String sql = "DELETE FROM Observations WHERE id = ?";
        try (Connection connection = DBConnectionLab5_1.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                observationsData.removeIf(observation -> observation.getID().equals(id));
            } else {
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateObservation(int id, Date observationDate, Double temperature, Double pressure) {
        String sql = "UPDATE Observations SET observation_date = ?, temperature = ?, pressure = ? WHERE id = ?";
        try (Connection connection = DBConnectionLab5_1.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, new java.sql.Date(observationDate.getTime()));
            preparedStatement.setDouble(2, temperature);
            preparedStatement.setDouble(3, pressure);
            preparedStatement.setInt(4, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Update local cache
                for (MeteorologicalObservations observation : observationsData) {
                    if (observation.getID().equals(id)) {
                        observation.observationDate = observationDate;
                        observation.temperature = temperature;
                        observation.pressure = pressure;
                        break;
                    }
                }
            } else {
                System.out.println("No observation found with the provided ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeObservations() {
        observationsData.clear();
        String sql = "SELECT * FROM Observations ORDER BY observation_date ASC";
        try (Connection connection = DBConnectionLab5_1.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Date observationDate = resultSet.getDate("observation_date");
                Double temperature = resultSet.getDouble("temperature");
                Double pressure = resultSet.getDouble("pressure");

                observationsData.add(new MeteorologicalObservations(id, observationDate, temperature, pressure));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Getters
    public Integer getID() {
        return ID;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getPressure() {
        return pressure;
    }
}

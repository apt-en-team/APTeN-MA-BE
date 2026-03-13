package com.apt.vehicle.mapper;

import com.apt.vehicle.model.Vehicle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VehicleMapper {

    List<Vehicle> findByUserId(Long userId);
    Vehicle findById(Long vehicleId);
    List<Vehicle> findAll();
    int countByHouseholdId(Long householdId);
    int existsByLicensePlate(String licensePlate);
    Long findIdByLicensePlate(String licensePlate);
    void insertVehicle(Vehicle vehicle);
    void updateVehicle(Vehicle vehicle);
    void deleteVehicle(Long vehicleId);
    void approveVehicle(Vehicle vehicle);
    void rejectVehicle(Long vehicleId);
    int countByStatus(String status);
}
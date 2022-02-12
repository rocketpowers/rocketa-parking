package com.pakingpackages.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ParkingSpotDto {

	@NotBlank
	private String parkingSpotNumber;

	@NotBlank
	@Size(max = 7)
	private String LicensePlateCar;

	@NotBlank
	private String brandCar;

	@NotBlank
	private String modelCar;

	@NotBlank
	private String colorCar;

	private LocalDateTime registrationDate;

	@NotBlank
	private String responsibleName;
	@NotBlank
	private String apartment;

	@NotBlank
	private String block;

}

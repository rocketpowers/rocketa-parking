package com.pakingpackages.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pakingpackages.dtos.ParkingSpotDto;
import com.pakingpackages.model.ParkingSpotModel;
import com.pakingpackages.services.ParkingSpotService;

@RestController
@RequestMapping("/parkingSpot")
public class ParkingSpotController {

	@Autowired
	ParkingSpotService parkingSpotService;

	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {

		if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			System.out.println("license plate car is already in use");
			return ResponseEntity.status(HttpStatus.CONFLICT).body("license plate car is already in use");

		}

		if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("parking spot is already in use ");
		}

		if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("already registered for this apartment/block");
		}

		var ParkingSpotModel = new ParkingSpotModel();
		// ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, ParkingSpotModel);
		ParkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(ParkingSpotModel));

	}

	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.ASC)Pageable pageable) {
		System.out.println("all registers listed");
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parking spot nor found");

		}
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parking spot not found");

		}
		parkingSpotService.delete(parkingSpotModelOptional.get());
		System.out.println("user deleted sucessfully");
		return ResponseEntity.status(HttpStatus.OK).body("parking spot deleted successfuly");

	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
			@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parking spot not found");
		}
		// var parkingSpotModel = parkingSpotModelOptional.get();
		// parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
		// parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
		// parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
		// parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
		// parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
		// parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName());
		// parkingSpotModel.setApartment(parkingSpotDto.getApartment());
		// parkingSpotModel.setBlock(parkingSpotDto.getBlock());

		var parkingSpotModel = new ParkingSpotModel();

		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
		parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());

		System.out.println("refresh sucessfully");

		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
	}

}

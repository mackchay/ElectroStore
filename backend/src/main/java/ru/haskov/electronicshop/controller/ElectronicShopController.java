package ru.haskov.electronicshop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.haskov.electronicshop.model.dto.BestEmployeeDto;
import ru.haskov.electronicshop.model.dto.TopSellerDto;
import ru.haskov.electronicshop.service.CsvImportService;
import ru.haskov.electronicshop.service.ElectronicShopService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/electroshop")
public class ElectronicShopController {
    private final ElectronicShopService electronicShopService;
    private final CsvImportService csvImportService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadZip(@RequestParam("file") MultipartFile file) {
        try {
            csvImportService.importFromZip(file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Electronic shop created");
    }

    @GetMapping("/top-seller")
    public ResponseEntity<List<TopSellerDto>> getBestEmployeeByPosiitonAndItem(
            @RequestParam("positionName") String positionName,
            @RequestParam("electroTypeName") String electroTypeName
    ) {
         return ResponseEntity.ok(electronicShopService.getBestEmployeesByPositionAndItem(
                 positionName, electroTypeName)
         );
    }

    @GetMapping("/top-employees")
    public ResponseEntity<List<BestEmployeeDto>> getBestEmployees() {
        return ResponseEntity.ok(electronicShopService.getBestEmployees());
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getRevenue(
            @RequestParam("purchaseType") String purchaseType
    ) {
        return ResponseEntity.ok(electronicShopService.getRevenue(purchaseType));
    }

}

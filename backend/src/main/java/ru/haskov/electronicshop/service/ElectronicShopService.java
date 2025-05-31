package ru.haskov.electronicshop.service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.haskov.electronicshop.model.dto.BestEmployeeDto;
import ru.haskov.electronicshop.model.dto.TopSellerDto;
import ru.haskov.electronicshop.repository.PurchaseRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ElectronicShopService {
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public List<TopSellerDto> getBestEmployeesByPositionAndItem(String positionName, String electroTypeName) {
        return purchaseRepository.findTopSeller(positionName, electroTypeName);
    }

    @Transactional
    public BigDecimal getRevenue(String purchaseTypeName) {
        return purchaseRepository.getTotalCashSalesLastYear(purchaseTypeName);
    }

    @Transactional
    public List<BestEmployeeDto> getBestEmployees() {
        return purchaseRepository.findBestEmployeesLastYear();
    }

}

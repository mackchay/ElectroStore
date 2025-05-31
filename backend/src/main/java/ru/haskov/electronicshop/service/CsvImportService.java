package ru.haskov.electronicshop.service;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.haskov.electronicshop.db.entity.*;
import ru.haskov.electronicshop.repository.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
@AllArgsConstructor
public class CsvImportService {

    // Репозитории для всех сущностей (должны быть внедрены через @Autowired)
    private final ElectroTypeRepository electroTypeRepo;
    private final ShopRepository shopRepo;
    private final PositionTypeRepository positionTypeRepo;
    private final EmployeeRepository employeeRepo;
    private final ElectroItemRepository electroItemRepo;
    private final PurchaseTypeRepository purchaseTypeRepo;
    private final PurchaseRepository purchaseRepo;
    private final ElectroEmployeeRepository electroEmployeeRepo;
    private final ElectroShopRepository electroShopRepo;

    // Порядок импорта для соблюдения зависимостей FK
    private static final List<String> IMPORT_ORDER = Arrays.asList(
            "ElectroType",
            "PositionType",
            "Shop",
            "Employee",
            "ElectroItem",
            "PurchaseType",
            "Purchase",
            "ElectroEmployee",
            "ElectroShop"
    );

    @Transactional
    public void importFromZip(MultipartFile zipFile) throws IOException {
        Path tempDir = Files.createTempDirectory("zip-import");
        try {
            // 1. Распаковка архива
            unzip(zipFile.getInputStream(), tempDir);

            // 2. Подготовка данных для валидации FK
            Map<Class<?>, Set<Object>> existingKeys = new HashMap<>();
            existingKeys.put(ElectroType.class, new HashSet<>());
            existingKeys.put(Shop.class, new HashSet<>());
            existingKeys.put(PositionType.class, new HashSet<>());
            existingKeys.put(Employee.class, new HashSet<>());
            existingKeys.put(ElectroItem.class, new HashSet<>());
            existingKeys.put(PurchaseType.class, new HashSet<>());
            existingKeys.put(Purchase.class, new HashSet<>());
            existingKeys.put(ElectroShop.class, new HashSet<>());
            existingKeys.put(ElectroEmployee.class, new HashSet<>());

            // 3. Импорт в правильном порядке
            for (String entityName : IMPORT_ORDER) {
                Path csvPath = findCsvFile(tempDir, entityName);
                if (csvPath != null) {
                    importCsv(csvPath, entityName, existingKeys);
                }
            }
        } finally {
            // 4. Очистка временных файлов
            deleteDirectory(tempDir);
        }
    }

    private void unzip(InputStream zipStream, Path outputDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(zipStream, StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = outputDir.resolve(entry.getName());
                if (!entry.isDirectory()) {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private Path findCsvFile(Path dir, String entityName) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.csv")) {
            for (Path path : stream) {
                if (path.getFileName().toString().equalsIgnoreCase(entityName + ".csv")) {
                    return path;
                }
            }
        }
        return null;
    }

    private void importCsv(Path csvPath, String entityName, Map<Class<?>, Set<Object>> existingKeys)
            throws IOException {
        List<Record> records = parseCsv(csvPath);

        for (Record record : records) {
            try {
                switch (entityName) {
                    case "ElectroType" -> importElectroType(record, existingKeys);
                    case "PositionType" -> importPositionType(record, existingKeys);
                    case "Shop" -> importShop(record, existingKeys);
                    case "Employee" -> importEmployee(record, existingKeys);
                    case "ElectroItem" -> importElectroItem(record, existingKeys);
                    case "PurchaseType" -> importPurchaseType(record, existingKeys);
                    case "Purchase" -> importPurchase(record, existingKeys);
                    case "ElectroEmployee" -> importElectroEmployee(record, existingKeys);
                    case "ElectroShop" -> importElectroShop(record, existingKeys);
                }
            } catch (Exception e) {
                // Логирование ошибки с продолжением обработки
                System.err.printf("Ошибка в строке %d: %s%n", records.indexOf(record), e.getMessage());
            }
        }
    }

    private List<Record> parseCsv(Path path) throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setDelimiterDetectionEnabled(true, ';');
        settings.setHeaderExtractionEnabled(true);
        settings.setLineSeparatorDetectionEnabled(true);
        settings.setIgnoreTrailingWhitespaces(true);
        settings.setSkipEmptyLines(true);
        settings.setColumnReorderingEnabled(true);
        settings.setNullValue("");
        settings.setEmptyValue("");

        CsvParser parser = new CsvParser(settings);
        try (Reader reader = new InputStreamReader(
                new FileInputStream(String.valueOf(path)), Charset.forName("Windows-1251"))) {
            return parser.parseAllRecords(reader);
        }
    }

    // Реализация методов импорта для каждой сущности
    private void importElectroType(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (electroTypeRepo.existsById(id)) return; // Защита от дубликатов PK

        ElectroType entity = new ElectroType();

        entity.setName(record.getString("name"));

        electroTypeRepo.save(entity);
        electroTypeRepo.flush();
        existingKeys.get(ElectroType.class).add(id);
    }

    private void importPositionType(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (positionTypeRepo.existsById(id)) return;

        PositionType entity = new PositionType();

        entity.setName(record.getString("name"));

        positionTypeRepo.save(entity);
        positionTypeRepo.flush();
        existingKeys.get(PositionType.class).add(id);
    }

    private void importShop(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (shopRepo.existsById(id)) return;

        Shop entity = new Shop();

        entity.setName(record.getString("name"));
        entity.setAddress(record.getString("address"));

        shopRepo.save(entity);
        shopRepo.flush();
        existingKeys.get(Shop.class).add(id);
    }

    private void importEmployee(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (employeeRepo.existsById(id)) return;

        Long positionId = Long.parseLong(record.getString("position"));
        Long shopId = Long.parseLong(record.getString("shopId"));

        // Проверка FK
        if (!existingKeys.get(PositionType.class).contains(positionId) ||
                !existingKeys.get(Shop.class).contains(shopId)) {
            System.err.printf("Нарушение FK для Employee ID: %s%n", id);
            return;
        }

        Employee entity = new Employee();

        entity.setLastName(record.getString("lastname"));
        entity.setFirstName(record.getString("firstname"));
        entity.setPatronymic(record.getString("patronymic"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        entity.setBirthDate(LocalDate.parse(record.getString("birthDate"), formatter));
        entity.setPositionType(positionTypeRepo.getReferenceById(positionId));
        entity.setGender(Boolean.parseBoolean(record.getString("gender")));

        employeeRepo.save(entity);
        employeeRepo.flush();
        existingKeys.get(Employee.class).add(id);
    }

    private void importElectroItem(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (electroItemRepo.existsById(id)) return;

        Long typeId = Long.parseLong(record.getString("etypeId"));
        if (!existingKeys.get(ElectroType.class).contains(typeId)) {
            System.err.printf("Нарушение FK для ElectroItem ID: %s%n", id);
            return;
        }

        ElectroItem entity = new ElectroItem();

        entity.setName(record.getString("name"));
        entity.setElectroType(electroTypeRepo.getReferenceById(typeId));
        entity.setPrice(Long.parseLong(record.getString("price")));
        entity.setCount(Integer.parseInt(record.getString("count")));
        entity.setArchive(Boolean.parseBoolean(record.getString("archive")));
        entity.setDescription(record.getString("description"));

        electroItemRepo.save(entity);
        electroItemRepo.flush();
        existingKeys.get(ElectroItem.class).add(id);
    }

    private void importPurchaseType(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (purchaseTypeRepo.existsById(id)) return;

        PurchaseType entity = new PurchaseType();

        entity.setName(record.getString("name"));

        purchaseTypeRepo.save(entity);
        purchaseTypeRepo.flush();
        existingKeys.get(PurchaseType.class).add(id);
    }

    private void importPurchase(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long id = Long.parseLong(record.getString("id"));
        if (purchaseRepo.existsById(id)) return;
        Long electroItemId = Long.parseLong(record.getString("electroId"));
        Long employeeId = Long.parseLong(record.getString("employeeId"));
        Long typeId = Long.parseLong(record.getString("typeId"));
        Long shopId = Long.parseLong(record.getString("shopId"));
        if (!existingKeys.get(PurchaseType.class).contains(typeId) ||
                !existingKeys.get(Shop.class).contains(shopId) ||
                !existingKeys.get(ElectroItem.class).contains(electroItemId) ||
                !existingKeys.get(Employee.class).contains(employeeId)) {
            System.err.printf("Нарушение FK для Purchase ID: %s%n", id);
            return;
        }

        Purchase entity = new Purchase();

        entity.setElectroItem(electroItemRepo.getReferenceById(electroItemId));
        entity.setEmployee(employeeRepo.getReferenceById(employeeId));
        entity.setPurchaseType(purchaseTypeRepo.getReferenceById(typeId));
        entity.setShop(shopRepo.getReferenceById(shopId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        entity.setPurchaseDate(LocalDateTime.parse(record.getString("purchaseDate"),
                formatter));

        purchaseRepo.save(entity);
        purchaseRepo.flush();
        existingKeys.get(Purchase.class).add(id);
    }

    private void importElectroEmployee(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long employeeId = Long.parseLong(record.getString("employeeId"));
        Long electroTypeId = Long.parseLong(record.getString("etype"));
        ElectroEmployeePK employeePK = new ElectroEmployeePK(employeeId, electroTypeId);

         if (electroEmployeeRepo.existsById(employeePK)) return;

        if (!existingKeys.get(Employee.class).contains(employeeId) ||
                !existingKeys.get(ElectroType.class).contains(electroTypeId)) {
            System.err.printf("Нарушение FK для ElectroEmployee [%s/%s]%n", employeeId, electroTypeId);
            return;
        }

        ElectroEmployee entity = new ElectroEmployee();
        entity.setId(employeePK);
        entity.setElectroType(electroTypeRepo.getReferenceById(electroTypeId));
        entity.setEmployee(employeeRepo.getReferenceById(employeeId));
        electroEmployeeRepo.save(entity);
        electroEmployeeRepo.flush();
        existingKeys.get(ElectroEmployee.class).add(employeePK);
    }


    private void importElectroShop(Record record, Map<Class<?>, Set<Object>> existingKeys) {
        Long shopId = Long.parseLong(record.getString("shopId"));
        Long itemId = Long.parseLong(record.getString("electroItemId"));
        ElectroShopPK shopPK = new ElectroShopPK(shopId, itemId);

        // Проверка существования записи
        if (electroShopRepo.existsById(shopPK)) return;

        // Проверка FK
        if (!existingKeys.get(Shop.class).contains(shopId) ||
                !existingKeys.get(ElectroItem.class).contains(itemId)) {
            System.err.printf("Нарушение FK для ElectroShop [%s/%s]%n", shopId, itemId);
            return;
        }

        ElectroShop electroShop = new ElectroShop();
        electroShop.setElectroShopPK(shopPK);
        electroShop.setCount(Integer.parseInt(record.getString("count")));
        electroShop.setShop(shopRepo.getReferenceById(shopId));
        electroShop.setElectroItem(electroItemRepo.getReferenceById(itemId));

        electroShopRepo.save(electroShop);
        electroShopRepo.flush();
        existingKeys.get(ElectroShop.class).add(shopPK);
    }

    // Аналогичные методы для других сущностей...

    // Вспомогательные методы
    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            System.err.println("Ошибка при удалении: " + p);
                        }
                    });
        }
    }
}

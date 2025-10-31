package co.edu.umanizales.smartdelivery.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CsvService<T> {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    @Value("${csv.file.path:./data/}")
    protected String csvFilePath;

    protected abstract String getCsvFileName();
    protected abstract Class<T> getEntityClass();

    public List<T> findAll() {
        List<T> items = new ArrayList<>();
        try {
            Path path = Paths.get(csvFilePath + getCsvFileName());
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                            .withType(getEntityClass())
                            .withIgnoreLeadingWhiteSpace(true)
                            .withIgnoreEmptyLine(true)
                            .withOrderedResults(false)
                            .build();
                    items = csvToBean.parse();
                    logger.info("Se cargaron {} registros desde {}", items.size(), path);
                }
            } else {
                logger.info("Archivo CSV no encontrado, se creará uno nuevo: {}", path);
                // Asegurarse de que el directorio existe
                Files.createDirectories(path.getParent());
                // Crear un archivo vacío con el encabezado
                saveAll(items);
            }
        } catch (Exception e) {
            logger.error("Error al leer archivo CSV: {}", getCsvFileName(), e);
            throw new RuntimeException("Error al leer el archivo CSV: " + e.getMessage(), e);
        }
        return items;
    }

    public void saveAll(List<T> items) {
        try {
            Path filePath = Paths.get(csvFilePath + getCsvFileName());
            
            // Asegurarse de que el directorio existe
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(filePath.toFile()), StandardCharsets.UTF_8)) {
                
                StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                        .withQuotechar('"')
                        .withSeparator(',')
                        .withApplyQuotesToAll(false)
                        .withOrderedResults(true)
                        .build();
                
                beanToCsv.write(items);
                logger.info("Se guardaron {} registros en {}", items.size(), filePath);
            }
        } catch (Exception e) {
            logger.error("Error al guardar archivo CSV: {}", getCsvFileName(), e);
            if (e instanceof CsvDataTypeMismatchException || e instanceof CsvRequiredFieldEmptyException) {
                logger.error("Error de formato en los datos al guardar el CSV: {}", e.getMessage());
            }
            throw new RuntimeException("Error al guardar el archivo CSV: " + e.getMessage(), e);
        }
    }

    public T save(T item) {
        Objects.requireNonNull(item, "El item a guardar no puede ser nulo");
        List<T> items = findAll();
        items.add(item);
        saveAll(items);
        return item;
    }
}

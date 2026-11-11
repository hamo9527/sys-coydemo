package com.cailu.bom.erp.interfaces;

import com.cailu.bom.common.api.ApiResponse;
import com.cailu.bom.common.api.PageResult;
import com.cailu.bom.erp.application.CreateProductCommand;
import com.cailu.bom.erp.application.ProductApplicationService;
import com.cailu.bom.erp.application.ProductImportResult;
import com.cailu.bom.erp.application.ProductImportRow;
import com.cailu.bom.erp.application.ProductView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/erp/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Map<String, List<String>> IMPORT_HEADERS = Map.of(
            "sku", List.of("商品编码", "*商品编码", "商品编号", "sku"),
            "name", List.of("商品名称", "*商品名称", "name"),
            "brandName", List.of("品牌名称", "*品牌名称", "品牌", "brandname"),
            "unit", List.of("单位", "*单位", "计量单位", "unit"),
            "productAttribute", List.of("商品属性", "*商品属性", "productattribute"),
            "mnemonicCode", List.of("助记码", "mnemoniccode"),
            "category", List.of("商品类别", "商品分类", "category"),
            "spec", List.of("商品规格", "规格", "spec"),
            "remark", List.of("备注", "remark")
    );

    private final ProductApplicationService productApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<ProductView>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.list(keyword, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductView>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductView>> update(
            @PathVariable Long id, @Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = toCommand(request);
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.update(id, command)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productApplicationService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductView>> create(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = toCommand(request);        return ResponseEntity.ok(ApiResponse.success(productApplicationService.create(command)));
    }


    private CreateProductCommand toCommand(CreateProductRequest request) {
        return new CreateProductCommand(
                request.sku(), request.mnemonicCode(), request.name(), request.brandName(),
                request.category(), request.spec(), request.unit(), request.auxiliaryUnit(),
                request.auxiliarySpec(), request.packagingUnit(), request.packagingSpec(),
                request.unitWeight(), request.minimumOrderQty(), request.minStock(), request.maxStock(),
                request.smallColorStock(), request.stockAgeThreshold(), request.obsoleteDays(),
                request.shelfLifeDays(), request.productAttribute(), request.virtualProduct(),
                request.installationRequired(), request.cloudStoreVisible(), request.productionProcess(),
                request.productionOrigin(), request.usageLocation(), request.vrUrl(), request.remark(),
                request.purchasePrice(), request.salePrice());
    }

    @GetMapping("/imports/excel/template")
    public ResponseEntity<byte[]> downloadExcelTemplate() throws IOException {
        String[] headers = {"*商品编码", "*商品名称", "*品牌名称", "*单位", "*商品属性",
                "助记码", "商品类别", "商品规格", "备注"};
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("商品档案导入模板");
            Row header = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            style.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, Math.min(40, Math.max(14, headers[i].length() * 2 + 4)) * 256);
            }
            sheet.createFreezePane(0, 1);
            workbook.write(output);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product-import-template.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(output.toByteArray());
        }
    }

    @PostMapping("/imports")
    public ResponseEntity<ApiResponse<ProductImportResult>> importRows(
            @RequestBody List<ProductImportRow> rows) {
        return ResponseEntity.ok(ApiResponse.success(productApplicationService.importProducts(rows)));
    }

    @PostMapping(value = "/imports/excel/preview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<ProductImportRow>>> previewExcel(
            @RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("请选择 Excel 文件");
        }
        return ResponseEntity.ok(ApiResponse.success(readExcel(file)));
    }

    private List<ProductImportRow> readExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() < 2) {
                throw new IllegalArgumentException("Excel 至少需要一行表头和一行商品数据");
            }
            DataFormatter formatter = new DataFormatter();
            Row header = sheet.getRow(sheet.getFirstRowNum());
            Map<String, Integer> columns = new HashMap<>();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                String value = normalize(formatter.formatCellValue(header.getCell(i)));
                for (Map.Entry<String, List<String>> entry : IMPORT_HEADERS.entrySet()) {
                    if (entry.getValue().contains(value)) {
                        columns.put(entry.getKey(), i);
                    }
                }
            }
            for (String required : List.of("sku", "name", "brandName", "unit", "productAttribute")) {
                if (!columns.containsKey(required)) {
                    throw new IllegalArgumentException("Excel 缺少必需列：" + requiredLabel(required));
                }
            }

            List<ProductImportRow> rows = new ArrayList<>();
            for (int rowIndex = header.getRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                String sku = cellValue(row, columns.get("sku"), formatter);
                String name = cellValue(row, columns.get("name"), formatter);
                String brand = cellValue(row, columns.get("brandName"), formatter);
                String unit = cellValue(row, columns.get("unit"), formatter);
                String attribute = cellValue(row, columns.get("productAttribute"), formatter);
                String mnemonic = cellValue(row, columns.get("mnemonicCode"), formatter);
                String category = cellValue(row, columns.get("category"), formatter);
                String spec = cellValue(row, columns.get("spec"), formatter);
                String remark = cellValue(row, columns.get("remark"), formatter);
                if (List.of(sku, name, brand, unit, attribute, mnemonic, category, spec, remark)
                        .stream().allMatch(String::isBlank)) {
                    continue;
                }
                rows.add(new ProductImportRow(sku, name, brand, unit, attribute,
                        mnemonic, category, spec, remark));
            }
            if (rows.isEmpty()) {
                throw new IllegalArgumentException("Excel 中没有可导入的商品数据");
            }
            return rows;
        }
    }

    private String cellValue(Row row, Integer column, DataFormatter formatter) {
        return column == null ? "" : formatter.formatCellValue(row.getCell(column)).trim();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    private String requiredLabel(String field) {
        return switch (field) {
            case "sku" -> "商品编码";
            case "name" -> "商品名称";
            case "brandName" -> "品牌名称";
            case "unit" -> "单位";
            default -> "商品属性";
        };
    }
}

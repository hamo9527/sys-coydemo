package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ErpStockLedgerMapper extends BaseMapper<ErpStockLedgerDO> {

    @Select("""
            SELECT warehouse_id AS warehouseId,
                   product_id AS productId,
                   color_code AS colorCode,
                   batch_no AS batchNo,
                   grade,
                   bin_name AS binName,
                   SUM(change_qty) AS quantity
            FROM erp_stock_ledger
            WHERE warehouse_id = #{warehouseId}
              AND biz_date <= #{billDate}
            GROUP BY warehouse_id, product_id, color_code, batch_no, grade, bin_name
            HAVING SUM(change_qty) <> 0
            ORDER BY product_id, color_code, batch_no, grade, bin_name
            """)
    List<StockBalanceAtDateRow> selectBalancesAtDate(
            @Param("warehouseId") Long warehouseId,
            @Param("billDate") LocalDate billDate);
}

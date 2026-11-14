package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("erp_stock_count")
public class ErpStockCountDO extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String billNo;

    private Long warehouseId;

    private LocalDate billDate;

    private String status;

    private String remark;
}

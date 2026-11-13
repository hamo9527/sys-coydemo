package com.cailu.bom.erp.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ErpStockMapper extends BaseMapper<ErpStockDO> {

    @Select("""
            SELECT color_code
            FROM (
                SELECT color_code FROM erp_stock WHERE product_id = #{productId}
                UNION
                SELECT color_code
                FROM erp_purchase_order_item
                WHERE product_id = #{productId}
                UNION
                SELECT color_code
                FROM erp_outbound_order_item
                WHERE product_id = #{productId}
            ) history
            WHERE color_code <> ''
              AND color_code LIKE CONCAT('%', #{keyword}, '%')
            ORDER BY color_code
            LIMIT 20
            """)
    List<String> selectColorCodeSuggestions(@Param("productId") Long productId,
                                            @Param("keyword") String keyword);
}

package com.cailu.bom.oa.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cailu.bom.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("oa_approval")
public class OaApprovalDO extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    /** leave / purchase / expense / custom */
    private String bizType;

    private Long bizId;

    private Long applicantId;

    /** draft / pending / approved / rejected / cancelled */
    private String status;

    private String remark;
}

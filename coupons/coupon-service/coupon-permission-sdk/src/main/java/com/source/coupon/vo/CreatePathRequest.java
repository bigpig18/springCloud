package com.source.coupon.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 路径创建请求对象定义
 *
 * @author li
 * @date 2020/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePathRequest {

    private List<PathInfo> pathInfos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PathInfo{

        /**
         * 路径模式
         */
        private String pathPattern;
        /**
         * HTTP 方法类型
         */
        private String httpMethod;
        /**
         * 路径名称
         */
        private String pathName;
        /**
         * 服务名称
         */
        private String serviceName;
        /**
         * 操作模式 READ读 - WRITE写
         */
        private String opMod;
    }
}

package com.source.coupon;

import com.source.coupon.permission.PermissionClient;
import com.source.coupon.vo.CommonResponse;
import com.source.coupon.vo.CreatePathRequest;
import com.source.coupon.vo.CreatePathRequest.PathInfo;
import com.source.coupon.vo.PermissionInfo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * 描述: 权限注册组件
 *
 * @author li
 * @date 2020/9/12
 */
@Slf4j
public class PermissionRegistry {

    /**
     * 权限服务 sdk 客户端
     */
    private PermissionClient permissionClient;
    /**
     * 服务名称
     */
    private String serviceName;

    public PermissionRegistry(PermissionClient permissionClient, String serviceName) {
        this.permissionClient = permissionClient;
        this.serviceName = serviceName;
    }

    /**
     * 权限注册
     * @param infoList 注册信息
     * @return boolean
     */
    boolean registry(List<PermissionInfo> infoList){
        if (CollectionUtils.isEmpty(infoList)){
            return false;
        }
        List<PathInfo> pathInfos = infoList.stream().map(
                info -> CreatePathRequest.PathInfo.builder()
                .pathPattern(info.getUrl())
                .httpMethod(info.getMethod())
                .pathName(info.getDescription())
                .serviceName(serviceName)
                .opMod(info.getIsRead() ? OpModeEnum.READ.name() : OpModeEnum.WRITE.name())
                .build()
        ).collect(Collectors.toList());
        CommonResponse<List<Integer>> response = permissionClient.createPath(new CreatePathRequest(pathInfos));
        if (!CollectionUtils.isEmpty(response.getData())){
            log.info("registry path info: {}",response.getData());
            return true;
        }
        return false;
    }

}

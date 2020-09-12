package com.source.coupon;

import com.source.coupon.annotation.CouponPermission;
import com.source.coupon.annotation.IgnorePermission;
import com.source.coupon.vo.PermissionInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * 描述: 接口权限信息扫描器
 *
 * @author li
 * @date 2020/9/12
 */
@Slf4j
public class AnnotationScanner {

    private String pathPrefix;

    private static final String COM_SOURCE_COUPON = "com.source.coupon";

    public AnnotationScanner(String pathPrefix) {
        this.pathPrefix = trimPath(pathPrefix);
    }

    /**
     * 构造所有controller接口的权限信息
     * @param mappingMap 构建所需信息
     * @return 接口权限信息
     */
    List<PermissionInfo> scanPermission(Map<RequestMappingInfo,HandlerMethod> mappingMap){
        List<PermissionInfo> result = new ArrayList<>();
        mappingMap.forEach((mapInfo,method) ->
                result.addAll(buildPermissionInfos(mapInfo,method)));
        return result;
    }

    /**
     * 构造controller的权限信息
     * @param mapInfo 标识 @RequestMapping 对应的信息
     * @param handlerMethod 标识 @RequestMapping 对应方法的详情，包括方法、类、参数
     * @return {@link PermissionInfo}s
     */
    private List<PermissionInfo> buildPermissionInfos(RequestMappingInfo mapInfo, HandlerMethod handlerMethod){
        Method javaMethod = handlerMethod.getMethod();
        Class<?> baseClass = javaMethod.getDeclaringClass();

        // 忽略非 com.source.coupon 下的mapping
        if (!isSourceCouponPackage(baseClass.getName())){
            log.info("Ignore method: {}",javaMethod.getName());
            return Collections.emptyList();
        }
        // 判断是否忽略此方法
        IgnorePermission ignorePermission = javaMethod.getAnnotation(IgnorePermission.class);
        if (null != ignorePermission){
            log.debug("Ignore method: {}", javaMethod.getName());
            return Collections.emptyList();
        }
        // 取出权限注解
        CouponPermission couponPermission = javaMethod.getAnnotation(CouponPermission.class);
        if (null == couponPermission){
            // 如未标注 CouponPermission 且未标注 IgnorePermission, 在日志中记录
            log.error("lack @CouponPermission -> {}#{}",javaMethod.getDeclaringClass().getName(),javaMethod.getName());
            return Collections.emptyList();
        }
        // 取出url
        Set<String> urlSet = mapInfo.getPatternsCondition().getPatterns();
        // 取出method
        boolean isAllMethods = false;
        Set<RequestMethod> methodSet = mapInfo.getMethodsCondition().getMethods();
        if (CollectionUtils.isEmpty(methodSet)){
            isAllMethods = true;
        }
        List<PermissionInfo> infoList = new ArrayList<>();
        for (String url : urlSet) {
            // 如果支持的 HTTP METHOD 为全量
            if (isAllMethods){
                PermissionInfo info = buildPermissionInfo(
                        HttpMethodEnum.ALL.name(),
                        javaMethod.getName(),
                        this.pathPrefix + url,
                        couponPermission.readOnly(),
                        couponPermission.description(),
                        couponPermission.extra()
                        );
                infoList.add(info);
                continue;
            }
            // 支持部分 HTTP METHOD
            for (RequestMethod method : methodSet) {
                PermissionInfo info = buildPermissionInfo(
                        method.name(),
                        javaMethod.getName(),
                        this.pathPrefix + url,
                        couponPermission.readOnly(),
                        couponPermission.description(),
                        couponPermission.extra()
                );
                infoList.add(info);
                log.info("Permission detected: {}", info);
            }
        }
        return infoList;
    }

    /**
     * 构造单个接口的权限信息
     * @return {@link PermissionInfo}
     */
    private PermissionInfo buildPermissionInfo(String reqMethod,String javaMethod,
            String path,boolean readOnly,String desp,String extra){
        PermissionInfo info = new PermissionInfo();
        info.setMethod(reqMethod);
        info.setUrl(path);
        info.setIsRead(readOnly);
        // 如果注解中无描述，则使用方法名
        info.setDescription(StringUtils.isEmpty(desp) ? javaMethod : desp);
        info.setExtra(extra);
        return info;
    }

    /**
     * 判断当前类是否在定义的包中
     * @param className 类名
     * @return boolean
     */
    private boolean isSourceCouponPackage(String className){
        return className.startsWith(COM_SOURCE_COUPON);
    }

    /**
     * 保证path以 / 开头 且不以 / 斜杆结尾
     * @param path 路径
     * @return 格式化后的路径
     */
    private String trimPath(String path){
        if (StringUtils.isEmpty(path)){
            return "";
        }
        String symbol = "/";
        if (!path.startsWith(symbol)){
            path = symbol + path;
        }
        if (path.endsWith(symbol)){
            path = path.substring(0,path.length() - 1);
        }
        return path;
    }

}

package com.source.coupon.dao;

import com.source.coupon.entity.Path;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述: path表对应的dao接口
 *
 * @author li
 * @date 2020/9/11
 */
public interface PathDao extends JpaRepository<Path,Integer> {

    /**
     * 根据服务名称查找path记录
     * @param serviceName 服务名称
     * @return path s
     */
    List<Path> findAllByServiceName(String serviceName);

    /**
     * 根据 路径模式 + http请求类型 查询路径记录
     * @param pathPattern 路径
     * @param httpMethod http请求类型
     * @return path
     */
    Path findByPathPatternAndHttpMethod(String pathPattern,String httpMethod);
}

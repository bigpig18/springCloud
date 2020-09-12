package com.source.coupon.service;

import com.source.coupon.dao.PathDao;
import com.source.coupon.entity.Path;
import com.source.coupon.vo.CreatePathRequest;
import com.source.coupon.vo.CreatePathRequest.PathInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 路径相关服务功能实现
 *
 * @author li
 * @date 2020/9/11
 */
@Slf4j
@Service
public class PathService {

    private final PathDao pathDao;

    @Autowired
    public PathService(PathDao pathDao) {
        this.pathDao = pathDao;
    }

    /**
     * 添加新的path到数据表中
     * @param request {@link CreatePathRequest}
     * @return 数据记录主键信息
     */
    public List<Integer> createPath(CreatePathRequest request){
        // 得到前端传递进来的path信息
        List<PathInfo> pathInfos = request.getPathInfos();
        // 用来存储有效的path信息
        List<PathInfo> validRequest = new ArrayList<>(request.getPathInfos().size());
        // 通过serviceName 查询已有路径记录
        List<Path> currentPath = pathDao.findAllByServiceName(
                pathInfos.get(0).getServiceName()
        );
        if (!CollectionUtils.isEmpty(currentPath)){
            // 若已有记录，则判断是否有重复的记录，有就剔除
            for (PathInfo pathInfo : pathInfos) {
                boolean isValid = true;
                for (Path path : currentPath) {
                    if (path.getPathPattern().equals(pathInfo.getPathPattern()) && path.getHttpMethod().equals(pathInfo.getHttpMethod())){
                        isValid = false;
                        break;
                    }
                }
                if (isValid){
                    validRequest.add(pathInfo);
                }
            }
        } else {
            // 无记录则直接赋值
            validRequest = pathInfos;
        }
        List<Path> paths = new ArrayList<>(validRequest.size());
        // 构建path
        validRequest.forEach(p -> paths.add(new Path(
                p.getPathPattern(),p.getHttpMethod(),p.getPathName(),p.getServiceName(),p.getOpMod()
        )));
        // path存入数据库，返回数据记录id
        return pathDao.saveAll(paths).stream().map(Path::getId).collect(Collectors.toList());
    }

}

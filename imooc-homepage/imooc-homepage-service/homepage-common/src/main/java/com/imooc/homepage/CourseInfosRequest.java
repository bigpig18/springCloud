package com.imooc.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 课程信息请求对象定义
 * @author li
 * @date 2019/9/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfosRequest {

    private List<Long> ids;
}

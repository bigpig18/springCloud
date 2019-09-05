package com.imooc.homepage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 创建用户请求对象定义
 * @author li
 * @date 2019/9/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String userName;
    private String email;

    /**
     * 请求有效性验证
     * @return boolean
     */
    public boolean validate(){
        return StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(email);
    }
}

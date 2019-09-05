package com.imooc.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基本用户信息
 * @author li
 * @date 2019/9/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Long id;
    private String userName;
    private String email;

    public static UserInfo invalid(){
        return new UserInfo(-1L,"","");
    }
}

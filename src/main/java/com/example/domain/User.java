package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: User
 * @Description:
 * @Author: 刘苏义
 * @Date: 2023年04月29日22:34
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
public class User {
    String name;
    Integer age;
}

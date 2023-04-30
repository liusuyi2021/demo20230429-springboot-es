package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: Content
 * @Description:
 * @Author: 刘苏义
 * @Date: 2023年04月30日22:36
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    String title;
    String img;
    String price;
}

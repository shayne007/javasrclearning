package com.feng.redis.publishsubscribe;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
@Data
@AllArgsConstructor
public class MessageVO {
    private Long mid;
    private String content;
    private Long ownerUid;
    private Integer type;
    private Long otherUid;
    private Date createTime;
    private String ownerUidAvatar;
    private String otherUidAvatar;
    private String ownerName;
    private String otherName;


}

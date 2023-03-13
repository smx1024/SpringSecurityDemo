package com.sx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    @Select(" SELECT\n" +
            "            DISTINCT m.`perms`\n" +
            "        FROM\n" +
            "            sys_user_role ur\n" +
            "            LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`\n" +
            "            LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`\n" +
            "            LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`\n" +
            "        WHERE\n" +
            "            user_id = #{userid}\n" +
            "            AND r.`status` = 0\n" +
            "            AND m.`status` = 0\n")
    List<String> selectPermsByUserId(Long userid);
}

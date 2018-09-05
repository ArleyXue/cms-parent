package com.arley.cms.console.util;

import com.arley.cms.console.pojo.vo.XTreeVO;
import com.arley.cms.console.pojo.Do.SysPermissionDO;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单工具类
 *
 * @author chen
 * @date 2018-01-02 17:54
 */
public class MenuUtil {


    /**
     * 判断菜单是否被勾选
     * @param originMenus 原菜单
     * @param roleMenus   角色对应菜单
     * @return
     */
    public static Set<XTreeVO> makeXTreeList(List<SysPermissionDO> originMenus, List<SysPermissionDO> roleMenus){
        Set<XTreeVO> trees = new LinkedHashSet <>();
        boolean checked;
        boolean disabled;
        for(SysPermissionDO sysPermission : originMenus){
            checked = false;
            disabled = sysPermission.getMenuState() != 1;
            if (roleMenus != null && roleMenus.size() > 0) {
                for(SysPermissionDO roleSysPermission : roleMenus){
                    if(sysPermission.getPermissionId().equals(roleSysPermission.getPermissionId())){
                        checked = true;
                        break;
                    }
                }
            }
            trees.add(new XTreeVO(sysPermission.getPermissionId(), sysPermission.getMenuName(),sysPermission.getParentId(), checked, disabled));
        }
        return eachTree(trees);
    }

    /**
     * 将已转成Tree对象的list进行转换成树状
     */
    private static Set<XTreeVO> eachTree(Set<XTreeVO> trees){
        Set<XTreeVO> rootTrees = new LinkedHashSet<>();
        for (XTreeVO tree : trees) {
            if(tree.getParentId() == null){
                rootTrees.add(tree);
            }
            for (XTreeVO t : trees) {
                if(tree.getValue().equals(t.getParentId())){
                    if(tree.getData().size() == 0){
                        List<XTreeVO> myChildrens = new ArrayList<>();
                        myChildrens.add(t);
                        tree.setData(myChildrens);
                    }else{
                        tree.getData().add(t);
                    }
                }
            }
        }
        return rootTrees;
    }

}
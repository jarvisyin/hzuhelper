package com.hzuhelper.model.receive;

import java.util.List;

import com.hzuhelper.model.RECEIVE;

/** 
 * 菜单项
 * MenuItem
 * @author jarvisyin
 *
 */
public class P6002 extends RECEIVE {
    private List<ARRAY_P6002> ARRAY_P6002;

    public List<ARRAY_P6002> getARRAY_P6002(){
        return ARRAY_P6002;
    }

    public void setARRAY_P6002(List<ARRAY_P6002> aRRAY_P6002){
        ARRAY_P6002 = aRRAY_P6002;
    }

}

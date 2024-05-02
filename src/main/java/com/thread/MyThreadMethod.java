package com.thread;

import com.entity.ShangpinEntity;
import com.service.ShangpinService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 线程执行方法（做一些项目启动后 一直要执行的操作，比如根据时间自动更改订单状态，比如订单签收30天自动收货功能，比如根据时间来更改状态）
 */
public class MyThreadMethod extends Thread  {

    private ShangpinService shangpinService;
    public ShangpinService getShangpinService() {
        return shangpinService;
    }
    public void setShangpinService(ShangpinService shangpinService) {
        this.shangpinService = shangpinService;
    }


    public void run() {
        while (!this.isInterrupted()) {// 线程未中断执行循环
            try {
                Thread.sleep(5000); //每隔2000ms执行一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<ShangpinEntity> shangpinEntities = shangpinService.selectList(null);

            if(shangpinEntities != null && shangpinEntities.size() > 0){
                List list = new ArrayList<ShangpinEntity>();
                long time = new Date().getTime();
                for (ShangpinEntity shangpin :shangpinEntities) {
                    ShangpinEntity shangpinEntity = new ShangpinEntity();
                    shangpinEntity.setId(shangpin.getId());
                    if(shangpin.getShangpinTime().getTime() <= time){
                        shangpinEntity.setShangxiaTypes(2);
                    }else{
                        shangpinEntity.setShangxiaTypes(1);
                    }
                    list.add(shangpinEntity);
                }
                if(list != null && list.size()>0){
                    shangpinService.updateBatchById(list);
                }
            }


        }
    }
}

package org.wf.dp.dniprorada.base.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Богдан on 23.10.2015.
 */

@javax.persistence.Entity
public class FlowLink extends Entity {

    @ManyToOne(targetEntity = Flow_ServiceData.class)
    @JoinColumn(name = "nID_Flow_ServiceData")
    private Long nID_Flow_ServiceData;

    @Column
    private Long nID_Service;

    public Long getnID_Flow_ServiceData() {
        return nID_Flow_ServiceData;
    }

    public void setnID_Flow_ServiceData(Long nID_Flow_ServiceData) {
        this.nID_Flow_ServiceData = nID_Flow_ServiceData;
    }

    public Long getnID_Service() {
        return nID_Service;
    }

    public void setnID_Service(Long nID_Service) {
        this.nID_Service = nID_Service;
    }
}

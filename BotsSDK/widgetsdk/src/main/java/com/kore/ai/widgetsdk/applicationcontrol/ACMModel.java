package com.kore.ai.widgetsdk.applicationcontrol;

public class ACMModel {
    private ApplicationControl applicationControl;
    /**
     *
     * @return
     * The applicationControl
     */
    public ApplicationControl getApplicationControl() {
        if(applicationControl == null)
            applicationControl = new ApplicationControl();
        return applicationControl;
    }

}

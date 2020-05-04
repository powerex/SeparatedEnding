package ua.azbest.model;

import ua.azbest.comunication.Token;
import ua.azbest.machine.ActiveMachine;
import ua.azbest.visual.ModelRender;

public class VisualModel extends Model {

    private ModelRender render;

    public VisualModel(int clusterSize) {
        super(clusterSize);
    }

    public void setRender(ModelRender render) {
        this.render = render;
        if (getMachines().get(0).getCurrentState() instanceof ActiveMachine) render.drawMachineActive(0);
    }

    @Override
    public void noticeMachineSetActive(int i) {
        if (render != null)
            render.drawMachineActive(i);
    }

    @Override
    public void noticeMachineSetPassive(int i) {
        if (render != null)
            render.drawMachinePassive(i);
    }

    @Override
    public void noticeTokenSend(int i) {
        Token token = machines.get(i).getToken();
        if (render != null && token != null)
            render.drawToken(token, i);
    }
}

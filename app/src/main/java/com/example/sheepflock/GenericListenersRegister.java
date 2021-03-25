package com.example.sheepflock;

import java.util.ArrayList;
import java.util.List;

abstract public class GenericListenersRegister<Model,OnReceiveListener> {

    private List<OnReceiveListener> onReceiveListeners=new ArrayList<>();

    public void add(OnReceiveListener onReceiveListener){
        onReceiveListeners.add(onReceiveListener);
    }

    public void remove(OnReceiveListener onReceiveListener){
        onReceiveListeners.remove(onReceiveListener);
    }

    public void  sendToAll(Model model){
        for (OnReceiveListener onReceiveListener:onReceiveListeners){
            doForEachListener(onReceiveListener,model);
        }
    }

    protected abstract void doForEachListener(OnReceiveListener onReceiveListener,Model model);


/*
    @Override
    public void callAll(JSONObject jsonObject) throws Exception {
        checkType(jsonObject,SERVICE_ID);
        APIModel apiModel;

        for (OnReceiveListener onReceiveListener:onReceiveListeners){
            //onReceiveListener.onReceive(ad);
        }
    }*/
}

package school;

public abstract class MicrosoftObject {

    private String object_id;

    private boolean sync = true;

    public String getObjectId() {
        return object_id;
    }

    public void setObjectId(String object_id) {
        this.object_id = object_id;
    }

    public boolean isSync(){
        return sync;
    }

    public void setSync(boolean sync){
        this.sync = sync;
    }

    public void startSyncronizing(){
        sync=true;
    }

    public void stopSyncronizing(){
        sync=false;
    }
}

package com.example.sheepflock.system;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ContentHandler {
    Context context;
    private String fileName2;

    protected ContentHandler(Context context, String fileName2 ){
        this.context=context;
        this.fileName2=fileName2;
    }

    protected Object getObject(String fileName) throws Exception {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        Object object = is.readObject();
        is.close();
        fis.close();
        return object;
    }

    protected void saveObject(Serializable object, String fileName) throws Exception {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(object);
        os.close();
        fos.close();
    }

    /*public static URI saveFile(File file,String fileName, byte[] bytes) {
        FileOutputStream fos = null;
        tryy {
            fos = new FileOutputStream(new File(file,fileName));
        fos.write(bytes);
        fos.close();
    } catch ( Exception e) {
            e.getStackTrace();
        }

        return file.toURI();
    }*/

    protected boolean isFileExist(String fileName){
       /* File file=new File(fileName);
        boolean t=file.exists();
        return file.exists();*/
       return context.getFileStreamPath(fileName).exists();
    }

    protected boolean deleteFile(String fileName){
        boolean xx=isFileExist(fileName);
        if(xx)
        context.getFileStreamPath(fileName).delete();
        return xx;
    }

    public boolean deleteFile(){
        if(fileName2==null)
            return false;
        return deleteFile(fileName2);
    }

/*protected <T> void saveAndAdd(T t)throws Exception{
        ArrayList<T> ts=(ArrayList<T>) getObject(fileName);
        ts.add(t);
        saveObject(ts,fileName);
    }*/
}

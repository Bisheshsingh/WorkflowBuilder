package TestModels;

import org.workflow.manager.models.ContextObject;

public class TestContext extends ContextObject {
    private String txt1;
    private String txt2;
    private String txt3;
    private String txt4;

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }

    public String getTxt3() {
        return txt3;
    }

    public void setTxt3(String txt3) {
        this.txt3 = txt3;
    }

    public String getTxt4() {
        return txt4;
    }

    public void setTxt4(String txt4) {
        this.txt4 = txt4;
    }

    @Override
    public String toString() {
        return "TestContext{" +
                "txt1='" + txt1 + '\'' +
                ", txt2='" + txt2 + '\'' +
                ", txt3='" + txt3 + '\'' +
                ", txt4='" + txt4 + '\'' +
                '}';
    }
}

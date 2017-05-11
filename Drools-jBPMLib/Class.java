public class Sensor {
    private String type;
    private String id;
    private float Value;

    public void setValue(float v) {
        this.Value = v;
    }

    public float getValue() {
        return this.Value;
    }
}

public class SmokeSensor extends Sensor {
    public void setValue(float value) {
        setValue(value);
    }

    public void getValue() {
        return getValue();
    }
}



public class Drools {
    KieServices ks;
    KieContainer kContainer;
    KieSession kSession;

    Drools() {}

    public void Initializer() {
        ks = KieServices.Factory.get();
        kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("ksession-process");
    }

    public void start() {
        kSession.startProcess("cep-process");
    }
}



public static void main(String[] args) {
    SmokeSensor smokeSensor;
    
    // Serial 통신 부분
    smokeSensor.setValue(val);

    // 제어 부분

    // 시작
    Drools drls;
    drls.Initializer();
    drls.start();
}

public void 화재제어(Boolean isFanOn, float Temprature, string cmd) {
    

}
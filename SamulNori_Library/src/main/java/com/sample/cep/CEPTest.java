package com.sample.cep;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CEPTest {
    void connect (String portName) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            //클래스 이름을 식별자로 사용하여 포트 오픈
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                //포트 설정(통신속도 설정. 기본 9600으로 사용)
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                //Input,OutputStream 버퍼 생성 후 오픈
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                 //읽기, 쓰기 쓰레드 작동
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    //데이터 송신
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    public static final void main(String[] args) {
        try {
			StatefulKnowledgeSession kSession = loadDrlWithCEP();
			
			
			
			FanActuatorData fad = new FanActuatorData(false);
			kSession.insert(fad);
			EntryPoint ep1 = kSession.getEntryPoint("thermometer");		
			EntryPoint ep2 = kSession.getEntryPoint("gas");
			EntryPoint ep3 = kSession.getEntryPoint("fingerprint");
			EntryPoint ep4 = kSession.getEntryPoint("vibration");
			//EntryPoint ep5 = kSession.getEntryPoint("dust");
			EntryPoint ep5 = kSession.getEntryPoint("value");

			
			// Rxtx
			new Rxtx().connect("COM7");
			SmokeSensorData ssd = new SmokeSensorData(102);
			ep2.insert(ssd);
			kSession.fireAllRules();
			kSession.halt();
			kSession.dispose();
			System.out.println(ssd.geta());
			/*
			for(int i = 0; i < THERMO_DATA.length; i++) {
				System.out.println(i);
				Thread.sleep(1000);

				ThermoSensorData tsd = new ThermoSensorData(THERMO_DATA[i]);
				ep1.insert(tsd);
				System.out.println("  T: " + THERMO_DATA[i]);
				kSession.fireAllRules();
				Thread.sleep(100);
				
				SmokeSensorData ssd = new SmokeSensorData(SMOKE_DATA[i]);
				ep2.insert(ssd);
				System.out.println("  S: " + SMOKE_DATA[i]);
				kSession.fireAllRules();
			}
			
			kSession.halt();
			kSession.dispose();
			*/
			
        } 
        
        catch (Throwable t) {
            t.printStackTrace();
        }
        
    }
    static KieSession loadDrl() {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();
		
		kfs.write(ResourceFactory.newClassPathResource("rules/cep.drl", CEPTest.class));
		
		KieBuilder kb = ks.newKieBuilder(kfs);
		
		kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
		if (kb.getResults().hasMessages(Message.Level.ERROR)) {
		    throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
		
		KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		
		KieSession kSession = kContainer.newKieSession();

		return kSession;
		
    }
    
    static StatefulKnowledgeSession loadDrlWithCEP() {
		KieBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rules/cep.drl", CEPTest.class), ResourceType.DRL);
		if (kbuilder.hasErrors()) {
		    throw new RuntimeException("Build Errors:\n" + kbuilder.getErrors().toString());
		}
		
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());  
		
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		return ksession;
    }
}

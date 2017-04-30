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

/**
 * This is a sample class to launch a rule.
 */
public class CEPTest {
	final static double[] THERMO_DATA = new double[] {
			20.0, 19.0, 23.3, 85.2, 49.5, 20.0, 21.2, 80.5, 90.9, 74.3, 38.2, 37.1, 20.0, 88.0, 79.0
	};

	final static double[] SMOKE_DATA = new double[] {
			22.0, 08.2, 03.3, 05.2, 01.5, 02.0, 01.2, 18.5, 19.9, 12.3, 12.8, 09.9, 08.8, 17.9, 18.1
	};

    public static final void main(String[] args) {
        try {
			StatefulKnowledgeSession kSession = loadDrlWithCEP();
			
			FanActuatorData fad = new FanActuatorData(false);
			kSession.insert(fad);
			EntryPoint ep1 = kSession.getEntryPoint("thermometer");		// 온도 센서 입력 스트림
			EntryPoint ep2 = kSession.getEntryPoint("smoke");			// 연기 센서 입력 스트림
			
			// 센서 값이 지속적으로 들어온다고 가정
			for(int i = 0; i < THERMO_DATA.length; i++) {
				System.out.println(i);
				Thread.sleep(1000);
				//	두 개의 센서 값이 각각 다른 시점에 들어온다고 가정
				ThermoSensorData tsd = new ThermoSensorData(THERMO_DATA[i]);
				ep1.insert(tsd);
				System.out.println("  T: " + THERMO_DATA[i]);
				kSession.fireAllRules();
				Thread.sleep(100);
				//	두 개의 센서 값이 각각 다른 시점에 들어온다고 가정
				SmokeSensorData ssd = new SmokeSensorData(SMOKE_DATA[i]);
				ep2.insert(ssd);
				System.out.println("  S: " + SMOKE_DATA[i]);
				kSession.fireAllRules();
			}
			
			kSession.halt();
			kSession.dispose();
        } catch (Throwable t) {
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

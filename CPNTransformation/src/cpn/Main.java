package cpn;

import java.util.HashMap;
import java.util.List;

import cpn.model.CPNNet;
import cpn.model.Module;

public class Main {
	
	public static void main(String[] args) throws Exception {
		//load("file:\\Users\\Matias\\Skule\\Master\\CPN Models\\ImplementationTestModels\\DebugModels\\DebugSubsTransFlatten.cpn");
		load("file:\\Users\\Matias\\Skule\\ExampleCPNModels\\SimpleProtocolHierInstScala.cpn");
	}
	
	private static void load(final String file) throws Exception {
		
		TransformNet tnet = new TransformNet(file);
		CPNNet net = tnet.transformNet();
		
		for(Module module : net.getModules().values()) {
			System.out.println(module.toString());
			System.out.println();
		}
	}
}

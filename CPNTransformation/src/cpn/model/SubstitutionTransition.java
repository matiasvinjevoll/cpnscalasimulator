package cpn.model;

import java.util.List;

public class SubstitutionTransition {
	private Module module;
	private List<Argument> args;


	public SubstitutionTransition(Module module, List<Argument> args) {
		super();
		this.module = module;
		this.args = args;
	}
	public List<Argument> getArgs() {
		return args;
	}
	public void setArgs(List<Argument> args) {
		this.args = args;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(module.getName() + ", args: ");
		for(Argument a : args)
			sb.append("[" +a + "], ");
		return sb.toString();
	}
}

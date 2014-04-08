package com.github.floppywaste.java8.scripting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class NashornTest {

	@Test
	public void helloJava() throws Exception {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("nashorn");
		engine.eval("print('test')");
	}

}

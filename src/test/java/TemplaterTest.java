import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class TemplaterTest {
	@Test
	public void TemplaterSimpleInput() {
		Templater Temp = new Templater("Hello {{name}} here. I am a {{job}} and I am {{age}} years old.");
		HashMap<String,  String> context = new HashMap<String, String>();
		context.put("name", "Honza");
		context.put("job", "Ninja");
		context.put("age", "18");
		assertEquals("Hello Honza here. I am a Ninja and I am 18 years old.", Temp.render(context));
	}
	@Test
	public void TemplaterRecursiveInput() {
		Templater Temp = new Templater("{{name{{age}}}}");
		HashMap<String,  String> context = new HashMap<String, String>();
		context.put("name", "Honza");
		context.put("age", "18");
		assertEquals("{{name18}}", Temp.render(context));
	}
	@Test
	public void TemplaterCyclonicRiftageInput() {
		Templater Temp = new Templater("{{var1}} {{var2}} {{{{var1}}}} {{{{val2}}}}");
		HashMap<String,  String> context = new HashMap<String, String>();
		context.put("val1", "val2");
		context.put("var1", "val1");
		assertFalse(Temp.render(context) == "val1 val2 val2 {{val2}}");
	}
}

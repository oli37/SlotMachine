package webapp;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * A Designer generated component for the test- template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("test-")
@HtmlImport("src/main/test-.html")
public class Test extends PolymerTemplate<Test.TestModel> {

    /**
     * Creates a new Test.
     */
    public Test() {
        // You can initialise any data required for the connected UI components here.
    }

    /**
     * This model binds properties between Test and test-
     */
    public interface TestModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
